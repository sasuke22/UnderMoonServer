package com.test.jwj.underMoon.bean;

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
    public boolean read;
    public boolean approve;
    public String type;
    public String loveType;
    public int age;
    public int marry;
    public int height;
    public String job;
    public String figure;
    public String xingzuo;
    public String content;
    public String registId;
    public String enlistersName;
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLoveType() {
		return loveType;
	}

	public void setLoveType(String loveType) {
		this.loveType = loveType;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getMarry() {
		return marry;
	}

	public void setMarry(int marry) {
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

	public String getFigure() {
		return figure;
	}

	public void setFigure(String figure) {
		this.figure = figure;
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

    public MeetingDetail(int meetingId, int id, String city, String summary, Date date, boolean read, boolean approve) {
        this.meetingId = meetingId;
        this.id = id;
        this.city = city;
        this.summary = summary;
        this.date = date;
        this.read = read;
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

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isApprove() {
		return approve;
	}

	public void setApprove(boolean approve) {
		this.approve = approve;
	}
	
	public String getEnlistersName() {
		return enlistersName;
	}

	public void setEnlistersName(String enlistersName) {
		this.enlistersName = enlistersName;
	}

	public String toString(){
		return "city " + city + " id " + id + " meetingId " + meetingId;
	}

	public String getRegistId() {
		return registId;
	}

	public void setRegistId(String registId) {
		this.registId = registId;
	}
    
}
