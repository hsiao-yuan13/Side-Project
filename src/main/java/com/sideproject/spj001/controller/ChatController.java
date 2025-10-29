package com.sideproject.spj001.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sideproject.spj001.entity.ChatVO;
import com.sideproject.spj001.security.CurrentUser;
import com.sideproject.spj001.service.AuthService;
import com.sideproject.spj001.service.ChatService;

@Controller
@RequestMapping("/frontend/chat")
public class ChatController {

	@Autowired
	private ChatService chatSvc;

	@Autowired
	private AuthService authSvc;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@GetMapping("/checkLogin")
	@ResponseBody
	public ResponseEntity<Boolean> isLoggedIn(Principal principal) {
		return ResponseEntity.ok(principal != null);
	}

	@GetMapping("/currentUser")
	@ResponseBody
	public CurrentUser getCurrentUser() {
		return authSvc.getCurrentUser();
	}

	@GetMapping("/receiverInfo")
	@ResponseBody
	public ChatVO getReceiverInfo(@RequestParam String roomId, Principal principal) {
		if (principal == null) {
			return null;
		}
		CurrentUser sender = authSvc.getCurrentUser();
		if(sender == null) {
			return null;
		}
		ChatVO chatVO = new ChatVO();
		chatVO.setRoomId(roomId);
		chatSvc.setReceiverInfo(chatVO, sender);
		return chatVO;
	}

	@GetMapping("/roomList")
	@ResponseBody
	public List<ChatVO> showChatList(Principal principal) {
		System.out.println("進入 /chat/roomList");
		System.out.println("Principal = " + principal);
		if (principal == null) {
			System.out.println("Principal is null");

			return Collections.emptyList();

		}
		CurrentUser sender = authSvc.getCurrentUser();
		if(sender == null) {
			return Collections.emptyList();
		}

		List<ChatVO> roomList = chatSvc.showChatList(sender);
		System.out.println("roomList size = " + (roomList != null ? roomList.size() : 0));

		return roomList;
	}

	@GetMapping("/roomId")
	@ResponseBody
	public String getRoomId(@RequestParam("receiver") Integer receiverId, Principal principal) {

		if (principal == null) {
			return null;
		}

		CurrentUser sender = authSvc.getCurrentUser();
		if(sender == null) {
			return null;
		}
		String receiverRole = chatSvc.getRoleById(receiverId);
		String roomId = chatSvc.getRoomId(sender, receiverId, receiverRole);

		return roomId;
	}

	@MessageMapping("/chat.send")
	public void sendChat(@Payload ChatVO chatVO, Principal principal) {
		if (principal == null) {
			System.out.println("principal為null，訊息不處理");
			return;
		}

	    CurrentUser sender = (CurrentUser) ((Authentication) principal).getPrincipal();
//		if(sender == null) {
//			System.out.println("找不到使用者，訊息無法傳送");
//			return;
//		}
		System.out.println("收到的 chatVO = " + chatVO);
		System.out.println("roomId = " + chatVO.getRoomId());

		if (chatVO.getRoomId() == null || chatVO.getRoomId().isEmpty()) {
			String receiverRole = chatSvc.getRoleById(chatVO.getReceiverId());
			String roomId = chatSvc.getRoomId(sender, chatVO.getReceiverId(), receiverRole);
			chatVO.setRoomId(roomId);
		}

		chatVO.setSenderId(sender.getId());
		chatVO.setSenderRole(sender.getRole());
		chatVO.setSenderName(sender.getName());
		chatVO.setTimestamp(LocalDateTime.now());

		chatSvc.saveChat(chatVO);

		messagingTemplate.convertAndSend("/topic/" + chatVO.getRoomId(), chatVO);
	}

	@GetMapping("/history/{roomId}")
	@ResponseBody
	public List<ChatVO> getChatHistory(@PathVariable String roomId) {
		return chatSvc.getHistory(roomId);
	}
}
