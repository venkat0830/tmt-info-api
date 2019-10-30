package com.example.mangodb.test.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.example.mangodb.test.model.Record;
import com.example.mangodb.test.utilities.Constants;

@Repository
public class RecordRepositoryImpl implements RecordRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	public static String TRACK_MY_RECORD = "trackmyrecord";

	@Override
	public List<Record> getDocuments(String providerTin, String recordType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("providerDetails.providerTin").is(providerTin));
		query.addCriteria(Criteria.where("recordInfo.recordType").regex("^" + recordType + "$", "i"));
		return mongoTemplate.find(query, Record.class);
	}

//	@Override
//	public Integer getRecordStatusCount(String category, String providerTin, String recordType) {
//		Query query = new Query();
//		query.addCriteria(Criteria.where("providerDetails.providerTin").is(providerTin));
//		query.addCriteria(Criteria.where("recordInfo.recordType").regex("^" + recordType + "$", "i"));
//		Criteria criteria = new Criteria();
//		if (Constants.CATEGORY_UNDER_REVIEW.equals(criteria)) {
//			query.addCriteria(criteria.orOperator(Criteria.where("recordInfo.recordStatus").is(Constants.RE_SUBMITTED).regex("^" + Constants.RE_SUBMITTED + "$", "i"),
//					Criteria.where("recordInfo.recordStatus").is(Constants.IN_PROGRESS).regex("^" + Constants.IN_PROGRESS + "$", "i"),
//					Criteria.where("recordInfo.recordStatus").is(Constants.UNDER_REVIEW).regex("^" + Constants.UNDER_REVIEW + "$", "i")));
//		}
//		if (Constants.CATEGORY_RECENTLY_CLOSED.equals(criteria)) {
//			query.addCriteria(criteria.orOperator(Criteria.where("recordInfo.recordStatus").is(Constants.CLOSED),
//					Criteria.where("recordInfo.recordStatus").is(Constants.DECISION_OVERTURNED),
//					Criteria.where("recordInfo.recordStatus").is(Constants.DECISION_UPHELD)));
//		}
//		if (Constants.CATEGORY_REQUIRES_ATTENTION.equals(criteria)) {
//			query.addCriteria(criteria.orOperator(Criteria.where("recordInfo.recordStatus").is(Constants.REVISED),
//					Criteria.where("recordInfo.recordStatus").is(Constants.REJECTED),
//					Criteria.where("recordInfo.recordStatus").is(Constants.ACTION_REQUIRED)));
//		}
//		if (mongoTemplate.find(query, Record.class) == null || mongoTemplate.find(query, Record.class).isEmpty()) {
//			return 0;
//		}
//		return mongoTemplate.find(query, Record.class).size();
//	}
}
