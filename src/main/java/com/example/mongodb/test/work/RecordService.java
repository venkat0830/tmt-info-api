package com.example.mongodb.test.work;

import java.util.List;

import com.example.mangodb.test.model.RecordView;

public interface RecordService {
	
	List<RecordView> getDocuments(String providerTin, String recordType, String uuiD, String superUser, String internalUser)  throws Exception; 

}
