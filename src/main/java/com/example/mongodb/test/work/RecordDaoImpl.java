//package com.example.mongodb.test.work;
//
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.TimeZone;
//
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.stereotype.Repository;
//
//import com.mongodb.DBCollection;
//import com.mongodb.DBObject;
//import com.mongodb.QueryBuilder;
//
//@Repository
//public class RecordDaoImpl implements RecordDao {
//
//	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
//
//	private MongoTemplate mongotemplate;
//	private String reconCollection;
//
//	@Override
//	public List<DBObject> getDocuments(String providerTin, String recordType) throws Exception {
//		DBCollection dbCollection;
//		DBObject queryList = QueryBuilder.start().put("recordInfo.recordType").is(recordType.toUpperCase())
//				.and("providerDetails.providerTin").is(providerTin).get();
//		System.out.println("operated query in MongoDB: " + queryList);
//		int days;
//		if ("RECON".equals(recordType)) {
//			dbCollection = mongotemplate.getCollection(reconCollection);
//			days = 14;
//		} else {
//			System.out.println("RecordType not found");
//			throw new Exception("No record found");
//		}
//		List<DBObject> result = dbCollection.find(queryList).toArray();
//		List<DBObject> latest = recentData(result, recordType);
//		if (!latest.isEmpty()) {
//			System.out.println("successfully records received");
//		} else {
//			System.out.println("couldn't found any records");
//		}
//		List<DBObject> sorted = sortByAscending(latest);
//		return latestUniqueRecords(sorted);
//	}
//
//	private List<DBObject> recentData(List<DBObject> result, String recordType) {
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN);
//		LocalDate daysAgo = LocalDate.now(ZoneId.of("CSt6CDT"));
//		String days = "";
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//		sdf.setTimeZone(TimeZone.getTimeZone("CST"));
//		String currentDate = sdf.format(new Date());
//		if ("RECON".equalsIgnoreCase(recordType)) {
//			daysAgo = LocalDate.now(ZoneId.of("CST6CDT")).minusDays(14);
//			days = "fourteen";
//		}
//		for (Iterator<DBObject> iterator = result.iterator(); iterator.hasNext();) {
//			DBObject document = iterator.next();
//			String date = ((DBObject) document.get("recordInfo")).get("recordLastUpdateDate").toString();
//			((DBObject) document.get("recordInfo")).put("recordLeastupdateDate", date);
//			
//		}
//		return result;
//	}
//
//	private List<DBObject> sortByAscending(List<DBObject> result) {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
//		result.sort((o1, o2) -> LocalDateTime
//				.parse(((DBObject) o2.get("recordInfo")).get("recordLastUpdate").toString(), formatter)
//				.compareTo(LocalDateTime.parse(((DBObject) o1.get("recordInfo")).get("recordLastUpdate").toString(),
//						formatter)));
//		return result;
//	}
//
//	private List<DBObject> latestUniqueRecords(List<DBObject> result) {
//		List<DBObject> finalList = new ArrayList<DBObject>();
//		for (Iterator<DBObject> iterator = result.iterator(); iterator.hasNext();) {
//			DBObject document = iterator.next();
//			Object recordID = ((DBObject) document.get("recordInfo")).get("recordID");
//			boolean flag = true;
//			for (DBObject obj : finalList) {
//				Object tempID = ((DBObject) document.get("recordInfo")).get("recordID");
//				if (recordID.equals(tempID)) {
//					flag = false;
//				}
//			}
//			if (flag) {
//				document.removeField("_Id");
//				finalList.add(document);
//			}
//		}
//		return finalList;
//	}
//}
