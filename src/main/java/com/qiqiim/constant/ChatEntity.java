package com.qiqiim.constant;

import java.io.Serializable;

public class ChatEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private int userId;
	private int anotherId;
	private String time;
	private String content;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getAnotherId() {
		return anotherId;
	}
	public void setAnotherId(int anotherId) {
		this.anotherId = anotherId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
