package com.jinniu.back.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinniu.commonjn.model.AdminUser;
import com.jinniu.commonjn.service.AdminUserService;
import com.jinniu.commonjn.util.JsonResult;

@Controller
public class CenterController {

	@Autowired
	private AdminUserService adminUserService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String main(HttpServletRequest request) {
		AdminUser u = (AdminUser) request.getSession().getAttribute("USER");
		if (u == null) {
			return "login/login";
		}
		request.setAttribute("USER", u);
		return "login/home";
	}

	@RequestMapping(value = "/getLogin", method = RequestMethod.POST)
	public @ResponseBody JsonResult getLogin(String passport, String passwd, HttpServletRequest request)  throws Exception {
		JsonResult result = adminUserService.Login(passport, passwd, request);
		if (result.getStatus() == 1) {
			request.getSession().setAttribute("USER", result.getData());
		}
		return result;
	}

	@RequestMapping(value="/welcome",method= RequestMethod.GET)
    public String welcome() {
    	return "login/login";
    }
	
	@RequestMapping(value="/logout",method= RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		try {
			response.sendRedirect("/mgrweb/");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
