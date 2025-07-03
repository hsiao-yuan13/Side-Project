package com.sideproject.spj001.interceptor;

import java.util.List;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class memLoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		HttpSession session = req.getSession(false);
		
		String uri = req.getRequestURI();

		List<String> whiteList = List.of(
				"/fronted/index",
				"/fronted/mem/memLoginPage",
				"/fronted/mem/loginMem",
				"/fronted/mem/logoutMem",
				"/fronted/mem/memRegister"
				);
		
		boolean isLoginRequest = uri.startsWith("/fronted/shop/")|| 
				 uri.startsWith("/static/");

								
		if (whiteList.contains(uri) || (session != null && session.getAttribute("loginMem") != null)) {
			return true;
		} else {
			String originalURL = req.getRequestURI();
			String queryString = req.getQueryString();
			if(queryString != null) {
				originalURL += "?" + queryString;
			}
			req.getSession(true).setAttribute("redirectAfterLogin", originalURL);
			
			res.sendRedirect("/frontend/mem/memLoginPage");
			return false;
		}
	}
}
