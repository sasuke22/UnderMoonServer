package com.qiqiim.constant;

import java.io.Serializable;
import java.sql.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class ChatEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private int userId;
	private int anotherId;
	private Date time;
	private String content;
	private String name;
	
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
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
