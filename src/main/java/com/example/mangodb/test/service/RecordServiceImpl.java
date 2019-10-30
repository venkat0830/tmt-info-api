package com.example.mangodb.test.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mangodb.test.model.Record;
import com.example.mangodb.test.model.RecordStatus;
import com.example.mangodb.test.repository.RecordRepository;
import com.example.mangodb.test.utilities.Constants;

@Service
public class RecordServiceImpl implements RecordService {

	@Autowired
	RecordRepository recordRepository;

	@Override
	public List<Record> getDocuments(String providerTin, String recordType) {
		return recordRepository.getDocuments(providerTin, recordType);
	}

	@Override
	public RecordStatus getCountInfo(String providerTin) {
		RecordStatus recordStatus = new RecordStatus();
		try {

			List<Record> reconList = recordRepository.getDocuments(providerTin, "RECON");
			List<Record> pendList = recordRepository.getDocuments(providerTin, "PEND");
			recordStatus.setPendCount(pendList.size());
			recordStatus.setReconCount(reconList.size());

			int catReconUnderReviewCount = 0;
			int catReconRecentlyClosedCount = 0;
			int catReconRequiresAttentionCount = 0;
			for (Record record : reconList) {
				if (Constants.UNDER_REVIEW.equals(record.getRecordInfo().getRecordStatus())
						|| Constants.RE_SUBMITTED.equals(record.getRecordInfo().getRecordStatus())
						|| Constants.IN_PROGRESS.equals(record.getRecordInfo().getRecordStatus())) {
					catReconUnderReviewCount++;

				} else if (Constants.DECISION_OVERTURNED.equals(record.getRecordInfo().getRecordStatus())
						|| Constants.DECISION_UPHELD.equals(record.getRecordInfo().getRecordStatus())
						|| Constants.REJECTED.equals(record.getRecordInfo().getRecordStatus())
						|| Constants.CLOSED.equals(record.getRecordInfo().getRecordStatus())) {
					catReconRecentlyClosedCount++;
				} else if (Constants.REVISED.equals(record.getRecordInfo().getRecordStatus())
						|| Constants.ACTION_REQUIRED.equals(record.getRecordInfo().getRecordStatus())) {
					catReconRequiresAttentionCount++;
				}
			}
			recordStatus.setReconUnderReview(catReconUnderReviewCount);
			recordStatus.setReconRecentlyClosed(catReconRecentlyClosedCount);
			recordStatus.setReconRequiresAttention(catReconRequiresAttentionCount);

			int catPendUnderReviewCount = 0;
			int catPendRecentlyClosedCount = 0;
			int catPendRequiresAttentionCount = 0;
			for (Record pendRecord : pendList) {
				if (Constants.RE_ROUTE.equals(pendRecord.getRecordInfo().getRecordStatus())
						|| Constants.PEND_SUSPEND.equals(pendRecord.getRecordInfo().getRecordStatus())
						|| Constants.PEND_HOLD.equals(pendRecord.getRecordInfo().getRecordStatus())
						|| Constants.REASSIGN_WORK.equals(pendRecord.getRecordInfo().getRecordStatus())
						|| Constants.PEND_UNDER_REVIEW.equals(pendRecord.getRecordInfo().getRecordStatus())
						|| Constants.RE_SUBMITTED.equals(pendRecord.getRecordInfo().getRecordStatus())) {
					catPendUnderReviewCount++;

				} else if (Constants.CLOSED.equals(pendRecord.getRecordInfo().getRecordStatus())) {
					catPendRecentlyClosedCount++;
				} else if (Constants.ACTION_REQUIRED.equals(pendRecord.getRecordInfo().getRecordStatus())) {
					catPendRequiresAttentionCount++;
				}
			}
			recordStatus.setPendUnderReview(catPendUnderReviewCount);
			recordStatus.setPendRecentlyClosed(catPendRecentlyClosedCount);
			recordStatus.setPendRequiresAttention(catPendRequiresAttentionCount);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return recordStatus;

	}
}
