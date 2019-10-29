package com.qiqiim.webserver.user.service;

import java.util.List;

import com.qiqiim.constant.Message;

public interface MsgListService {
	List<Message> queryMessageList(int user_id);
	
	void insertMessage(int user_id,int another_id,String content);
	
	void deleteMessage(int user_id,int another_id);
	
	void readMessage(int user_id,int another_id);

	List<Integer> queryIfUnread(int userId);
}
