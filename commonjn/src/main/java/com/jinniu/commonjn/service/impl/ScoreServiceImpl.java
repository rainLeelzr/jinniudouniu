package com.jinniu.commonjn.service.impl;

import com.jinniu.commonjn.dao.ScoreDao;
import com.jinniu.commonjn.model.Score;
import com.jinniu.commonjn.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreServiceImpl extends BaseServiceImpl<Integer, Score> implements ScoreService {
	
	@Autowired
	private ScoreDao dao;

}