package com.example.mangodb.test.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trackmyrecord")
public class Record {
	
	@Id
	private String _id;
	private RecordInfo recordInfo;
	private ProviderDetails providerDetails;
	private MemberDetails memberDetails;
	private ClaimDetails claimDetails;
	public Record(RecordInfo recordInfo, ProviderDetails providerDetails, MemberDetails memberDetails,
			ClaimDetails claimDetails) {
		super();
		this.recordInfo = recordInfo;
		this.providerDetails = providerDetails;
		this.memberDetails = memberDetails;
		this.claimDetails = claimDetails;
	}
	public RecordInfo getRecordInfo() {
		return recordInfo;
	}
	public void setRecordInfo(RecordInfo recordInfo) {
		this.recordInfo = recordInfo;
	}
	public ProviderDetails getProviderDetails() {
		return providerDetails;
	}
	public void setProviderDetails(ProviderDetails providerDetails) {
		this.providerDetails = providerDetails;
	}
	public MemberDetails getMemberDetails() {
		return memberDetails;
	}
	public void setMemberDetails(MemberDetails memberDetails) {
		this.memberDetails = memberDetails;
	}
	public ClaimDetails getClaimDetails() {
		return claimDetails;
	}
	public void setClaimDetails(ClaimDetails claimDetails) {
		this.claimDetails = claimDetails;
	}
	
}
