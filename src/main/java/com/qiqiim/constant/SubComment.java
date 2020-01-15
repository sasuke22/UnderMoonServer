package com.qiqiim.constant;

import java.util.Date;

public class SubComment {
	private int floorId;//层主评论id，对应meetingcomments或者articlecomments的id
	private int userId;//子评论用户id
	private String content;//评论内容
	private Date date;//评论日期
	private int replyId;//回复谁的id
	private String replyName;//回复谁的名字
	private int showName;//是否显示名字
	public int getFloorId() {
		return floorId;
	}
	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getReplyId() {
		return replyId;
	}
	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}
	public String getReplyName() {
		return replyName;
	}
	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}
	public int getShowName() {
		return showName;
	}
	public void setShowName(int showName) {
		this.showName = showName;
	}
	
	
}
