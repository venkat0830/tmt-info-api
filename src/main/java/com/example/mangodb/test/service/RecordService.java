package com.example.mangodb.test.service;

import java.util.List;

import com.example.mangodb.test.model.Record;
import com.example.mangodb.test.model.RecordStatus;

public interface RecordService {

List<Record> getDocuments(String provider, String recordtype);
RecordStatus getCountInfo(String providerTin);
}
