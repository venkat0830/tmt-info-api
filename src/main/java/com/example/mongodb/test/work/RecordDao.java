package com.example.mongodb.test.work;

import java.util.List;

import com.mongodb.DBObject;

public interface RecordDao {
	
	List<DBObject> getDocuments(String providerTin, String recordType) throws Exception;

}
