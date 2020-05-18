package com.example.mangodb.test.repository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.example.mangodb.test.model.Record;
import com.example.mongodb.test.work.LocalDtTime;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class RecordDaoImpl implements RecordDao {

	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static final String ID = "_id";
	private static final String DATE = "recordLastUpdateDate";
	private static final String PREDICTION_END_DATE = "predictionEndDate";
	private static final String PREDICTION_START_DATE = "predictionStartDate";
	private static final String RECORD_INFO = "recordInfo";
	private static final String RECORD_INFO__RECORD_TYPE = "recordInfo.recordType";
	private static final String PROVIDER_DETAILS_PROVIDER_TIN = "providerDetails.providerTin";
	private static final String RECORD_ID = "recordID";

	private MongoTemplate mongotTemplate;
	private String reconCollection;
	private String pendCollection;
	private String hpcCollection;
	private String mrmCollection;
	private String smartEditsCollection;

////////////Office code
	@Override
	public List<DBObject> getDocuments(String providerTin, String recordType) throws Exception {
		DBCollection dbCollection;
		DBObject queryList = QueryBuilder.start().put(RECORD_INFO__RECORD_TYPE).is(recordType.toUpperCase())
				.and(PROVIDER_DETAILS_PROVIDER_TIN).is(providerTin).get();
		System.out.println("operated query in mongo DB:" + queryList);
		int days;
		if ("RECON".equals(recordType)) {
			dbCollection = mongotTemplate.getCollection(reconCollection);
			days = 14;
		} else if ("PEND".equals(recordType)) {
			dbCollection = mongotTemplate.getCollection(pendCollection);
			days = 14;
		} else if ("HPC".equals(recordType)) {
			dbCollection = mongotTemplate.getCollection(hpcCollection);
			days = 30;
		} else if ("MRM".equals(recordType)) {
			dbCollection = mongotTemplate.getCollection(mrmCollection);
			days = 60;
		} else if ("SMARTEDITS".equals(recordType)) {
			dbCollection = mongotTemplate.getCollection(smartEditsCollection);
			days = 60;
		} else {
			System.out.println("Record type not found:" + recordType);
			throw new Exception("Record not found");
		}
		List<DBObject> result = dbCollection.find(queryList).toArray();
		// Getting documents from the last described days.
		List<DBObject> latest = recentData(result, recordType);
		if (!latest.isEmpty()) {
			System.out.println("Successfully received the records");
		} else {
			System.out.println("couldn't find any records");
		}
		List<DBObject> sorted = sortByAscending(latest);
		return latestUniqueRecordIDs(sorted);
	}

	private List<DBObject> recentData(List<DBObject> result, String recordType) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN);
		LocalDate daysAgo = LocalDate.now(ZoneId.of("CST6CDT"));
		String days = "";
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("CST"));
		String currentdate = sdf.format(new Date());
		if ("RECON".equals(recordType) || ("PEND".equals(recordType))) {
			daysAgo = LocalDate.now(ZoneId.of("CST6CDT")).minusDays(14);
			days = "fourteen";
		} else if ("MRM".equals(recordType) || ("SMARTEDITS".equals(recordType))) {
			daysAgo = LocalDate.now(ZoneId.of("CST6CDT")).minusDays(60);
			days = "sixty";
		}
		for (Iterator<DBObject> iterator = result.iterator(); iterator.hasNext();) {
			DBObject document = iterator.next();
			LocalDtTime localDtTime = new LocalDtTime();
			String date = ((DBObject) document.get(RECORD_INFO)).get(DATE).toString();
			if ("RECON".equals(recordType)
					&& ((DBObject) document.get(RECORD_INFO)).get(RECORD_ID).toString().toUpperCase().startsWith("PIQ")
					&& localDtTime.isValidUnixDate(date)) {
				date = localDtTime.convertUnixDate(date);
				((DBObject) document.get(RECORD_INFO)).put(DATE, date);
			}
			if (!"HPC".equals(recordType) && !LocalDate.parse(date, dtf).isAfter(daysAgo)) {
				iterator.remove();
				System.out.println("Documents ignored whicha re older than:" + days + "days:" + daysAgo);

			}
			if ("HPC".equals(recordType) && isNotValidHPCClaim(document, currentdate)) {
				iterator.remove();
				System.out.println("Documents ignored as prediction end date is beyond the current date, recordID:"
						+ ((DBObject) document.get(RECORD_INFO)).get(RECORD_ID));
			}

		}
		return result;
	}

	private List<DBObject> sortByAscending(List<DBObject> result) {
		DateTimeFormatter formtter = DateTimeFormatter.ofPattern(DATE_PATTERN);
		result.sort((o1, o2) -> LocalDateTime.parse(((DBObject) o2.get(RECORD_INFO)).get(DATE).toString(), formtter)
				.compareTo(LocalDateTime.parse(((DBObject) o1.get(RECORD_INFO)).get(DATE).toString(), formtter)));
		return result;
	}

	private List<DBObject> latestUniqueRecordIDs(List<DBObject> result) {
		List<DBObject> finalList = new ArrayList<DBObject>();
		for (Iterator<DBObject> iterator = result.iterator(); iterator.hasNext();) {
			DBObject document = iterator.next();
			Object recordID = ((DBObject) document.get(RECORD_INFO)).get(RECORD_ID);
			boolean flag = true;
			for (DBObject obj : finalList) {
				Object tempID = ((DBObject) document.get(RECORD_INFO)).get(RECORD_ID);
				if (recordID.equals(tempID)) {
					flag = false;
				}
			}
			if (flag) {
				document.removeField(ID);
				finalList.add(document);
			}
		}
		return finalList;
	}

	public boolean isNotValidHPCClaim(DBObject document, String currentdateString) {
		try {
			boolean isNotValid = true;
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			if (null == ((DBObject) document.get(RECORD_INFO)).get(PREDICTION_END_DATE)
					|| null == ((DBObject) document.get(RECORD_INFO)).get(PREDICTION_START_DATE)) {
				return isNotValid;
			} else if (null != ((DBObject) document.get(RECORD_INFO)).get(PREDICTION_END_DATE)
					|| null != ((DBObject) document.get(RECORD_INFO)).get(PREDICTION_START_DATE)) {
				Date currentDate = sdf.parse(currentdateString);
				Date parseStartDate = sdf
						.parse(((DBObject) document.get(RECORD_INFO)).get(PREDICTION_START_DATE).toString());
				Date parsedEndDate = sdf
						.parse(((DBObject) document.get(RECORD_INFO)).get(PREDICTION_END_DATE).toString());
				if (currentDate.before(parseStartDate) || currentDate.after(parsedEndDate)) {
					return isNotValid;
				}
			}
			isNotValid = false;
			return isNotValid;
		} catch (Exception ex) {
			System.out.println("exception encountered:" + ex.getMessage());
			return true;
		}
	}

//////////////Office code
	@Override
	public List<DBObject> getRecordDocuments(String providerTin, String recordType) throws Exception {
		Set<String> records = new HashSet<String>();
		try {
			DBCollection dbCollection;
			if ("RECON".equals(recordType)) {
				dbCollection = mongotTemplate.getCollection(reconCollection);
			} else if ("PEND".equals(recordType)) {
				dbCollection = mongotTemplate.getCollection(pendCollection);
				
			} else if ("HPC".equals(recordType)) {
				dbCollection = mongotTemplate.getCollection(hpcCollection);
				} 
			BasicDBObject andQuery =  new BasicDBObject();
			BasicDBObject orQuery =  new BasicDBObject();
			BasicDBObject regualrDateQuery =  new BasicDBObject();
			BasicDBObject unixDateQuery =  new BasicDBObject();
			BasicDBObject predictionDateQuery =  new BasicDBObject();
			List<BasicDBObject> queryList =  new ArrayList<BasicDBObject>();
			List<BasicDBObject> dateQueryList =  new ArrayList<BasicDBObject>();
			if (!"HPC".equalsIgnoreCase(recordType)) {
				regualrDateQuery.append("$gte", LocalDtTime.getLastUpdatedDate(recordType,false));
				unixDateQuery.append("$gte", LocalDtTime.getLastUpdatedDate(recordType,true));
				regualrDateQuery.append("$lte", LocalDtTime.getTodaysDate(false));
				unixDateQuery.append("$lte", LocalDtTime.getTodaysDate(false));
				queryList.add(new BasicDBObject("providerDetails.providerTin", providerTin));
				queryList.add(new BasicDBObject("recordInfo.recordType", recordType));
				dateQueryList.add(new BasicDBObject("recordInfo.recordLastUpdateDate", regualrDateQuery));
				dateQueryList.add(new BasicDBObject("recordInfo.recordLastUpdateDate", unixDateQuery));
				orQuery.put("$or", dateQueryList);
				queryList.add(new BasicDBObject(orQuery));
				andQuery.put("$and", queryList);
					} 
			if ("HPC".equals(recordType)) {
		regualrDateQuery.append("$gte", LocalDtTime.getLastUpdatedDate(recordType, false));
		regualrDateQuery.append("$lte", LocalDtTime.getTodaysDate(false));
		predictionDateQuery.append("$gte",LocalDtTime.getPredDate());
		queryList.add(new BasicDBObject("recordInfo.predictionEndDate",predictionDateQuery));
		queryList.add(new BasicDBObject("providerDetails.providerTin", providerTin));
		queryList.add(new BasicDBObject("recordInfo.recordType", recordType));
		queryList.add(new BasicDBObject("recordInfo.recordLastUpdateDate", regualrDateQuery));
		andQuery.put("$and", queryList);
		}
		List<DBObject> result =  dbCollection.find(andQuery).toArray();
		System.out.println("operated Query:+"andQuery);
		for (DBObject dbObject : result) {
			Record reccord =  convertDBObjectToRecord(dbObjectList);
			records.add(Record.class);
		}
		} catch (Exception ex) {
			System.out.println("exception encountered:"+ex.getMessage());
		}
		return records;
	}

	private List<Record> convertDBObjectToRecord(List<DBObject> dbObjectList) throws Exception {
		List<Record> listOfRecords = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			for (DBObject dbObject : dbObjectList) {
				String dbObjectString = mapper.writeValueAsString(dbObject);
				Record record = mapper.readValue(dbObjectString, Record.class);
				Record record1 = new Record();
				record1.setClaimDetails(record.getClaimDetails());
				record1.setMemberDetails(record.getMemberDetails());
				record1.setProviderDetails(record.getProviderDetails());
				record1.setRecordInfo(record.getRecordInfo());
				listOfRecords.add(record1);
			}
		} catch (Exception ex) {
			throw ex;
		}
		return listOfRecords;
	}

}
