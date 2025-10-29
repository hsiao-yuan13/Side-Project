package com.sideproject.spj001.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.sideproject.spj001.interceptor.WebSocketAuthChannelInterceptor;
//import com.sideproject.spj001.interceptor.WebsocketHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer{

	@Autowired
	private WebSocketAuthChannelInterceptor webSocketAuthChannelInterceptor;
	
//	配置訊息代理message broker
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic", "/queue");
		config.setApplicationDestinationPrefixes("/app");
	}
	
//	註冊websocket連線端點，映射指定URL
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
				.setAllowedOriginPatterns("*")
//				.addInterceptors(new WebsocketHandshakeInterceptor())
				.withSockJS();
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration){
		registration.interceptors(webSocketAuthChannelInterceptor);
		
	}
}

