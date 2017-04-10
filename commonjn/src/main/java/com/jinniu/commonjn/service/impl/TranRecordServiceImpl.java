package com.jinniu.commonjn.service.impl;

import com.jinniu.commonjn.dao.TranRecordDao;
import com.jinniu.commonjn.model.TranRecord;
import com.jinniu.commonjn.service.TranRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TranRecordServiceImpl extends BaseServiceImpl<Integer, TranRecord> implements TranRecordService {
	
	@Autowired
	private TranRecordDao dao;

}