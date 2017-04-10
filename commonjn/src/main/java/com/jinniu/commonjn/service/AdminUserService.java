package com.jinniu.commonjn.service;

import javax.servlet.http.HttpServletRequest;

import com.jinniu.commonjn.model.AdminUser;
import com.jinniu.commonjn.util.JsonResult;

public interface AdminUserService extends BaseService<Integer, AdminUser> {

	JsonResult Login(String passport, String password, HttpServletRequest request)  throws Exception;

	JsonResult saveOrUpdate(AdminUser admin);
}