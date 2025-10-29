package com.sideproject.spj001.entity;

import java.time.LocalDateTime;

public class ChatVO {

	private Integer senderId;
	private String senderRole;
	private String senderName;
	
	private Integer receiverId;
	private String receiverRole;
	private String receiverName;
	
	private String roomId;
	private String content;
	private LocalDateTime timestamp;
	private String lastMsg;
	
	public ChatVO() {
		this.timestamp = LocalDateTime.now();
	}
	
	public String getRedisKey() {
		return "chat:" + roomId;
	}

	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public String getSenderRole() {
		return senderRole;
	}

	public void setSenderRole(String senderRole) {
		this.senderRole = senderRole;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	

	public Integer getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverRole() {
		return receiverRole;
	}

	public void setReceiverRole(String receiverRole) {
		this.receiverRole = receiverRole;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getLastMsg() {
		return lastMsg;
	}

	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}

	
	
	
	
	
}
