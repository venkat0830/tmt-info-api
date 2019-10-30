package com.example.mangodb.test.repository;


import java.util.List;

import com.example.mangodb.test.model.Record;

public interface RecordRepository {

	List<Record> getDocuments(String providerTin, String recordType);
//	Integer getRecordStatusCount(String category, String providerTin, String recordType);
}
