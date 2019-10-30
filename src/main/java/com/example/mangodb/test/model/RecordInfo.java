package com.example.mangodb.test.model;

public class RecordInfo {

	private String recordId;
	private String recordType;
	private String recordStatus;
	public RecordInfo(String recordId, String recordType, String recordStatus) {
		super();
		this.recordId = recordId;
		this.recordType = recordType;
		this.recordStatus = recordStatus;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	
	
	
}
