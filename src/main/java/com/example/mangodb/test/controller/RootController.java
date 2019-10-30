package com.example.mangodb.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mangodb.test.model.Record;
import com.example.mangodb.test.model.RecordStatus;
import com.example.mangodb.test.service.RecordService;



@RestController
public class RootController {

	@Autowired
	RecordService recordService;

	@GetMapping(value = "/summary")
	public RecordStatus getCount(@RequestParam("providerTin") String providerTin) {
		return recordService.getCountInfo(providerTin);
	}

	@GetMapping(value = "/detail")
	public List<Record> getSummary(@RequestParam("providerTin") String providerTin,
			@RequestParam("recordType") String recordType) {
		return recordService.getDocuments(providerTin, recordType);
	}
}
