package com.qiqiim.webserver.user.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.qiqiim.constant.MeetingDetail;

public class MeetingsModel implements Serializable{
	private static final long serialVersionUID = 2L;
	ArrayList<MeetingDetail> meetings;
	
	public MeetingsModel(ArrayList<MeetingDetail> meetings){
		this.meetings = meetings;
	}
}
