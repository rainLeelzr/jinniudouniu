package com.jinniu.back.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinniu.commonjn.model.AdminUser;
import com.jinniu.commonjn.model.Entity.AdminUserCriteria;
import com.jinniu.commonjn.model.Entity.Pagination;
import com.jinniu.commonjn.model.Entity.Value;
import com.jinniu.commonjn.service.AdminUserService;
import com.jinniu.commonjn.util.ErrorCode;
import com.jinniu.commonjn.util.JsonResult;

@Controller
@RequestMapping("/adminMgt")
public class AdminController {
	
	@Autowired
	private AdminUserService adminUserService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, AdminUser bean, Pagination pagination) {
		AdminUserCriteria criteria = new AdminUserCriteria();
		if (bean.getUserName() != null && !bean.getUserName().equals("")) {
			criteria.setUserName(Value.eq(bean.getUserName()));
		}
		Pagination list = adminUserService.selectPage(criteria, pagination);
		request.setAttribute("pagination", list);
		request.setAttribute("bean", bean);
		return "admin/list";
	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request) {
		return "admin/view";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String add(HttpServletRequest request, int id) {
		AdminUser adminUser = adminUserService.selectOne(id);
		if (adminUser == null) {
			// 待处理
			return "404";
		}else {
			request.setAttribute("bean", adminUser);
		}
		return "admin/view";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody JsonResult save(AdminUser admin) {
		return adminUserService.saveOrUpdate(admin);
	}
	
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public @ResponseBody JsonResult del(int id) {
		JsonResult result = new JsonResult();
		if (adminUserService.delete(id) < 1) {
			result.addErrorCode(ErrorCode.SYS_ERR);
			return result;
		}
		return result;
	}
}
