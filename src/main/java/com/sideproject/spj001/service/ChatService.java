package com.sideproject.spj001.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sideproject.spj001.entity.ChatVO;

@Service
public class ChatService {
	@Autowired
	@Qualifier("chatRedisTemplate")
	private RedisTemplate<String, String> chatRedisTemplate;
	
	private static final int MAX_HISTORY = 100;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
//	發送訊息(儲存)
	public void saveChat(ChatVO chatVO) {
		try {
		String key = "chat:" + chatVO.getRoomId();
		String json = objectMapper.writeValueAsString(chatVO);
		chatRedisTemplate.opsForList().rightPush(key, json);
		chatRedisTemplate.opsForList().trim(key, -MAX_HISTORY, -1);
		}catch(JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
//	顯示歷史紀錄
	public List<ChatVO> getHistory(String roomId){
		List<String> jsonList = chatRedisTemplate.opsForList().range("chat:" + roomId, 0, -1);
		
		if(jsonList == null || jsonList.isEmpty()) {
			return Collections.emptyList();
		}else {
			return jsonList.stream().map(json -> {
				try{
					return objectMapper.readValue(json, ChatVO.class);
				}catch(JsonProcessingException e) {
					e.printStackTrace();
					return null;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());
		}
	}

}
