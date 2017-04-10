package com.jinniu.back.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jinniu.commonjn.model.AdminUser;

/**
 * 	登陆检查拦截器
 * @author 
 *
 */
public class LoginCheckInterceptor extends HandlerInterceptorAdapter{

	private static final Logger LOG = LoggerFactory.getLogger(LoginCheckInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String requestMethod = request.getMethod(); // 获取请求方式
		LOG.info(String.format("requestMethod:%s", requestMethod));
		String url = request.getRequestURL().toString();
		System.out.println("url---"+ url);
		if (url.contains("getLogin") || url.contains("welcome")) {
			return true;
		}
		
		AdminUser admin = (AdminUser) request.getSession().getAttribute("USER");
		if (admin == null) {
			response.sendRedirect("/mgrweb/");
		}
		return true;
	}
}