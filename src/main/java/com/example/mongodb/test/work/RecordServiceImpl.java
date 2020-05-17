package com.example.mongodb.test.work;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.mangodb.test.model.Record;
import com.example.mangodb.test.model.RecordView;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;

public class RecordServiceImpl implements RecordService {

	@Autowired
	RecordDao recordDao;

	@Override
	public List<RecordView> getDocuments(String providerTin, String recordType, String uuID, String superUser,
			String internalUser) throws Exception {
		try {
			List<DBObject> dbObjectList = recordDao.getDocuments(providerTin, recordType);
			List<RecordView> listRecords = convertDBObjectToRecord(dbObjectList, uuID);
			List<RecordView> finalRecordsList = listRecords;
			if (!finalRecordsList.isEmpty()) {
				System.out.println("successfully received");
			} else {
				System.out.println("No records found");
			}
			return finalRecordsList;
		} catch (Exception ex) {
			System.out.println("exception encountered: " + ex.getMessage());
			throw ex;
		}

	}

	private List<RecordView> convertDBObjectToRecord(List<DBObject> dbObjectList, String uuID) throws Exception {
		List<RecordView> listRecords = new ArrayList<RecordView>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			for(DBObject dbObject : dbObjectList) {
				String dbObjectString = mapper.writeValueAsString(dbObject);
				Record record =  mapper.readValue(dbObjectString, Record.class);
				updateRecord(record, uuID);
				RecordView recordView = new RecordView();
				recordView.setProviderDetails(record.getProviderDetails());
				recordView.setMemberDetails(record.getMemberDetails());
				recordView.setRecordInfo(record.getRecordInfo());
				recordView.setClaimDetails(record.getClaimDetails());
				listRecords.add(recordView);
			}
		} catch (Exception ex) {
			throw ex;
		}
		return listRecords;
	}
	
	private void updateRecord(Record record, String uuID) {
		String recordType = record.getRecordInfo().getRecordStatus();
		String status =  record.getRecordInfo().getRecordStatus();
		if ("RECON".equals(recordType)) {
			if ("Under Review".equals(status)) {
				record.getRecordInfo().setRecordStatus("Under Review1");
			}
		} else {
			System.out.println("No records found for the record type recon");
		}
	}

}
