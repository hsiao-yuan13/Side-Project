package com.sideproject.spj001.interceptor;

import java.util.Map;



import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class WebsocketHandshakeInterceptor implements HandshakeInterceptor{

	@Override
	public boolean beforeHandshake(ServerHttpRequest serverReq, ServerHttpResponse serverRes, WebSocketHandler wsHandler, Map<String, Object> attributes)throws Exception{
		
		if(serverReq instanceof ServletServerHttpRequest servletServerReq){
			HttpServletRequest httpReq = servletServerReq.getServletRequest();
			HttpSession session = httpReq.getSession(false);
		
			
			if(session != null) {
			Object memId = session.getAttribute("memId");
			Object sellerId = session.getAttribute("sellerId");
		
		
		
				if(memId != null) {
					attributes.put("senderId", memId.toString());
					attributes.put("senderType", "MEMBER");
					
				}else if(sellerId != null) {
						attributes.put("senderId", sellerId.toString());
						attributes.put("senderType", "SELLER");
				}
			}
		
		}
		return true;
	}
	
	@Override
	public void afterHandshake(ServerHttpRequest serverReq, ServerHttpResponse serverRes, WebSocketHandler wsHandler, Exception ex) {
		
	}
	
	
}
