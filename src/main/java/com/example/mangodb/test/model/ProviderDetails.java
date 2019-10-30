package com.example.mangodb.test.model;

public class ProviderDetails {
	private String providerName;
	private String providerTin;
	public ProviderDetails(String providerName, String providerTin) {
		super();
		this.providerName = providerName;
		this.providerTin = providerTin;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getProviderTin() {
		return providerTin;
	}
	public void setProviderTin(String providerTin) {
		this.providerTin = providerTin;
	}
	

}
