package com.jinniu.commonjn.dao.impl;

import com.jinniu.commonjn.dao.UserDao;
import com.jinniu.commonjn.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends BaseDaoImpl<Integer, User> implements UserDao {

}