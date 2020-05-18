package com.example.mangodb.test.repository;

import java.util.List;

import com.mongodb.DBObject;

public interface RecordDao {
	
	List<DBObject> getDocuments(String providerTin, String recordType) throws Exception;
	List<DBObject> getRecordDocuments(String providerTin, String recordType) throws Exception;

}
