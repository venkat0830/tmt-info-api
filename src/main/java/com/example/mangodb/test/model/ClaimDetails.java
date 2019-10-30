package com.example.mangodb.test.model;

public class ClaimDetails {
	
	private String claimNumber;
	private String claimFromDateOfService;
	public ClaimDetails(String claimNumber, String claimFromDateOfService) {
		super();
		this.claimNumber = claimNumber;
		this.claimFromDateOfService = claimFromDateOfService;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getClaimFromDateOfService() {
		return claimFromDateOfService;
	}
	public void setClaimFromDateOfService(String claimFromDateOfService) {
		this.claimFromDateOfService = claimFromDateOfService;
	}
	
	

}
