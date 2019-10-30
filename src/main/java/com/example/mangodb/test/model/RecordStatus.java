package com.example.mangodb.test.model;

public class RecordStatus {

	private Integer reconUnderReview;
	private Integer reconRecentlyClosed;
	private Integer reconRequiresAttention;
	private Integer pendUnderReview;
	private Integer pendRecentlyClosed;
	private Integer pendRequiresAttention;
	private Integer reconCount;
	private Integer pendCount;

	public Integer getReconUnderReview() {
		return reconUnderReview;
	}

	public void setReconUnderReview(Integer reconUnderReview) {
		this.reconUnderReview = reconUnderReview;
	}

	public Integer getReconRecentlyClosed() {
		return reconRecentlyClosed;
	}

	public void setReconRecentlyClosed(Integer reconRecentlyClosed) {
		this.reconRecentlyClosed = reconRecentlyClosed;
	}

	public Integer getReconRequiresAttention() {
		return reconRequiresAttention;
	}

	public void setReconRequiresAttention(Integer reconRequiresAttention) {
		this.reconRequiresAttention = reconRequiresAttention;
	}

	public Integer getPendUnderReview() {
		return pendUnderReview;
	}

	public void setPendUnderReview(Integer pendUnderReview) {
		this.pendUnderReview = pendUnderReview;
	}

	public Integer getPendRecentlyClosed() {
		return pendRecentlyClosed;
	}

	public void setPendRecentlyClosed(Integer pendRecentlyClosed) {
		this.pendRecentlyClosed = pendRecentlyClosed;
	}

	public Integer getPendRequiresAttention() {
		return pendRequiresAttention;
	}

	public void setPendRequiresAttention(Integer pendRequiresAttention) {
		this.pendRequiresAttention = pendRequiresAttention;
	}

	public Integer getReconCount() {
		return reconCount;
	}

	public void setReconCount(Integer reconCount) {
		this.reconCount = reconCount;
	}

	public Integer getPendCount() {
		return pendCount;
	}

	public void setPendCount(Integer pendCount) {
		this.pendCount = pendCount;
	}

	public RecordStatus() {

	}
}
