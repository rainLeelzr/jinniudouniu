package com.jinniu.commonjn.service.impl;

import com.jinniu.commonjn.dao.VoteDao;
import com.jinniu.commonjn.model.Vote;
import com.jinniu.commonjn.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl extends BaseServiceImpl<Integer, Vote> implements VoteService {
	
	@Autowired
	private VoteDao dao;

}