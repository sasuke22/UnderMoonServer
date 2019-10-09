package com.qiqiim.constant;

import java.io.Serializable;
import java.util.ArrayList;

public class UserListModel implements Serializable{
	private static final long serialVersionUID = 2L;
	ArrayList<User> userList;
	
	public UserListModel(ArrayList<User> list){
		this.userList = list;
	}
}
