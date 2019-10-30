package com.example.mangodb.test.model;

public class MemberDetails {
	
	private String subscriberName;
	private String patientId;
	public MemberDetails(String subscriberName, String patientId) {
		super();
		this.subscriberName = subscriberName;
		this.patientId = patientId;
	}
	public String getSubscriberName() {
		return subscriberName;
	}
	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	

}
