package com.test.jwj.underMoon.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/20.
 */

public class MeetingDetail implements Serializable{
	
	private static final long serialVersionUID = 1L;
    public int meetingId;
    public int id;
    public String city;
    public String summary;
    public String date;
    public boolean read;
    public boolean approve;
    
    public MeetingDetail(){}

    public MeetingDetail(int meetingId, int id, String city, String summary, String date, boolean read, boolean approve) {
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
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
    
}
