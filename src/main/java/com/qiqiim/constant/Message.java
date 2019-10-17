package com.qiqiim.constant;

import java.sql.Date;

public class Message {
	private int userId;//用户id
	private int anotherId;//对方用户id
	private String content;//发送内容
	private String name;//对方名字
	private Date time;//发送时间
	private int unread;//未读消息数
	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getUnread() {
		return unread;
	}
	public void setUnread(int unread) {
		this.unread = unread;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
