package com.sideproject.spj001.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.spj001.dao.MemRepository;
import com.sideproject.spj001.dao.SellerRepository;
import com.sideproject.spj001.entity.ChatVO;
import com.sideproject.spj001.security.CurrentUser;

@Service
public class ChatService {
	@Autowired
	private MemRepository memRepository;

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	@Qualifier("chatRedisTemplate")
	private RedisTemplate<String, String> chatRedisTemplate;

	private static final int MAX_HISTORY = 100;

	@Autowired
	private final ObjectMapper objectMapper = new ObjectMapper();

	public boolean isUserInRoom(String roomId, CurrentUser sender) {
	if(roomId == null ||  sender == null){
		return false;
	}
		return roomId.contains(sender.getId() + "-" + sender.getRole());
	}
	
	public void setReceiverInfo(ChatVO chatVO, CurrentUser sender) {
		String roomId = chatVO.getRoomId();
		String[] tokens = roomId.split("-");

		if (tokens.length >= 4) {
			try {
				int id1 = Integer.parseInt(tokens[0]);
				String role1 = tokens[1];
				int id2 = Integer.parseInt(tokens[2]);
				String role2 = tokens[3];

				if (id1 == sender.getId() && role1.equals(sender.getRole())) {
					chatVO.setReceiverId(id2);
					chatVO.setReceiverRole(role2);
					chatVO.setReceiverName(getNameByIdAndRole(id2, role2));
				} else {
					chatVO.setReceiverId(id1);
					chatVO.setReceiverRole(role1);
					chatVO.setReceiverName(getNameByIdAndRole(id1, role1));
				}
			} catch (NumberFormatException e) {
				System.err.println("解析roomId失敗" + roomId);
			}
		}
	}

	
	public List<ChatVO> showChatList(CurrentUser sender) {
		Map<String, ChatVO> roomMap = new HashMap<>();
		Set<String> keys = chatRedisTemplate.keys("chat:*");

		if (keys == null || keys.isEmpty()) {
			return Collections.emptyList();
		}

		for (String key : keys) {
			String roomId = key.replace("chat:", "");
			

			
			if(!isUserInRoom(roomId, sender)) {
				continue;
			}
			
			try {
				List<String> msgs = chatRedisTemplate.opsForList().range(key, -1, -1);

				if (msgs != null && !msgs.isEmpty()) {
					String lastJson = msgs.get(0);

					ChatVO chatVO = objectMapper.readValue(lastJson, ChatVO.class);
					setReceiverInfo(chatVO, sender);
					roomMap.put(roomId, chatVO);
				}

			} catch (JsonProcessingException e) {
				System.err.println("解析訊息失敗" + key);
				e.printStackTrace();
			}

		}
		List<ChatVO> roomList = new ArrayList<>(roomMap.values());
		roomList.sort(Comparator.comparing(ChatVO::getTimestamp, Comparator.nullsLast(Comparator.reverseOrder())));

		return roomList;

	}
	
	private String getNameByIdAndRole(Integer id, String role) {
		if("ROLE_MEMBER".equals(role)) {
			return memRepository.findById(id).map(mem -> mem.getMemName()).orElse("未知會員");
			
		}else if("ROLE_SELLER".equals(role)) {
			return sellerRepository.findById(id).map(seller -> seller.getSellerName()).orElse("未知商家");
		}
		return "未知";
	}

	
	public String getRoleById(Integer receiverId) {
		if (memRepository.existsById(receiverId)) {
			return "ROLE_MEMBER";
		} else if (sellerRepository.existsById(receiverId)) {
			return "ROLE_SELLER";
		} else {
			throw new RuntimeException("使用者不存在");
		}
	}

	public String getRoomId(CurrentUser sender, Integer receiverId, String receiverRole) {

		if ("ROLE_MEMBER".equals(sender.getRole()) && "ROLE_SELLER".equals(receiverRole)) {
			return sender.getId() + "-ROLE_MEMBER-" + receiverId + "-ROLE_SELLER";
		} else if ("ROLE_SELLER".equals(sender.getRole()) && "ROLE_MEMBER".equals(receiverRole)) {
			return receiverId + "-ROLE_MEMBER-" + sender.getId() + "-ROLE_SELLER";
		} else {
			return sender.getId() < receiverId
					? sender.getId() + "-" + sender.getRole() + "-" + receiverId + "-" + receiverRole
					: receiverId + "-" + receiverRole + "-" + sender.getId() + "-" + sender.getRole();
		}

	}



//	發送訊息(儲存)
	public void saveChat(ChatVO chatVO) {
		try {

			String redisKey = "chat:" + chatVO.getRoomId();
			String json = objectMapper.writeValueAsString(chatVO);

			chatRedisTemplate.opsForList().rightPush(redisKey, json);
			chatRedisTemplate.opsForList().trim(redisKey, -MAX_HISTORY, -1);

//			
//			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

//	顯示歷史紀錄
	public List<ChatVO> getHistory(String roomId) {
		String redisKey = "chat:" + roomId;
		List<String> msg = chatRedisTemplate.opsForList().range(redisKey, 0, -1);

		if (msg == null || msg.isEmpty()) {
			return Collections.emptyList();
		} else {
			return msg.stream().map(json -> {
				try {
					return objectMapper.readValue(json, ChatVO.class);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return null;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());
		}
	}

}
