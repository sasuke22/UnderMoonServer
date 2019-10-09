package com.qiqiim.constant;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Administrator on 2017/7/20.
 */

public class MeetingDetail implements Serializable{
	
	private static final long serialVersionUID = 1L;
    public int meetingId;
    public int id;
    public String city;
    public String summary;
    public Date date;
    public int gender;
    public int approve;
    public int age;
    public String marry;
    public int height;
    public String job;
    public String xingzuo;
    public String content;
    public int commentCount;
    public int score;
    public int pics;
    public String reason;
    public int top;
    public boolean isVip;
    
    public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPics() {
		return pics;
	}

	public void setPics(int pics) {
		this.pics = pics;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getMarry() {
		return marry;
	}

	public void setMarry(String marry) {
		this.marry = marry;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getXingzuo() {
		return xingzuo;
	}

	public void setXingzuo(String xingzuo) {
		this.xingzuo = xingzuo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MeetingDetail(){}

    public MeetingDetail(int meetingId, int id, String city, String summary, Date date, int gender, int approve) {
        this.meetingId = meetingId;
        this.id = id;
        this.city = city;
        this.summary = summary;
        this.date = date;
        this.gender = gender;
        this.approve = approve;
    }

	public int getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(int meetingId) {
		this.meetingId = meetingId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int isApprove() {
		return approve;
	}

	public void setApprove(int approve) {
		this.approve = approve;
	}
	
	public String toString(){
		return "city " + city + " id " + id + " meetingId " + meetingId;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean isVip) {
		this.isVip = isVip;
	}
    
	
}
