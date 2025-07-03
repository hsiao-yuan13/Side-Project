package com.sideproject.spj001.entity;

import java.time.LocalDateTime;

public class ChatVO {

	private String sender;
	private String receiver;
	private String content;
	private String roomId;
	private LocalDateTime timestamp;
	
	public String getSender() {
		return sender;
	}
	
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
}
