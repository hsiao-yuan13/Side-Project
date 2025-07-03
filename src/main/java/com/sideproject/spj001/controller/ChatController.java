package com.sideproject.spj001.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sideproject.spj001.entity.ChatVO;
import com.sideproject.spj001.service.ChatService;

@Controller
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatService chatSvc;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@MessageMapping("/chat.send")
	public void sendChat(@Payload ChatVO chatVO) {
		chatVO.setTimestamp(LocalDateTime.now());
		chatSvc.saveChat(chatVO);
		
		messagingTemplate.convertAndSend("/topic/private" + chatVO.getRoomId(), chatVO);
	}
	
	@GetMapping("/chat/history/{roomId}")
	public List<ChatVO> getChatHistory(@PathVariable String roomId){
		return chatSvc.getHistory(roomId);
	}
}
