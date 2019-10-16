package com.qiqiim.constant;

import java.sql.Date;

public class Message {
	private int id;//自增id
	private int user_id;//用户id
	private int another_id;//对方用户id
	private String content;//发送内容
	private String name;//对方名字
	private Date time;//发送时间
	private int unread;//未读消息数
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
