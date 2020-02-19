package com.qiqiim.constant;

import java.util.Date;

public class Count {
	private int id;//人id
	private int talk;//当天聊天的陌生人个数
	private Date date;//日期
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTalk() {
		return talk;
	}
	public void setTalk(int talk) {
		this.talk = talk;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
