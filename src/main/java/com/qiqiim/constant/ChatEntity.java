package com.qiqiim.constant;

import java.io.Serializable;

public class ChatEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private int user_id;
	private int another_id;
	private String time;
	private String content;
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getAnother_id() {
		return another_id;
	}
	public void setAnother_id(int another_id) {
		this.another_id = another_id;
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
