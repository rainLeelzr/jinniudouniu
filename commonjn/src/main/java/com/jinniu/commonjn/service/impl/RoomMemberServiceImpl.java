package com.jinniu.commonjn.service.impl;

import com.jinniu.commonjn.dao.RoomMemberDao;
import com.jinniu.commonjn.model.RoomMember;
import com.jinniu.commonjn.service.RoomMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomMemberServiceImpl extends BaseServiceImpl<Integer, RoomMember> implements RoomMemberService {
	
	@Autowired
	private RoomMemberDao dao;

}