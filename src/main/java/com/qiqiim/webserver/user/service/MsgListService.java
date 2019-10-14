package com.qiqiim.webserver.user.service;

import java.util.List;

import com.qiqiim.constant.ChatEntity;
import com.qiqiim.constant.Message;

public interface MsgListService {
	List<Message> queryMessageList(int user_id);
	
	void insertMessage(ChatEntity chat);
	
	void deleteMessage(int user_id,int another_id);
	
	void readMessage(int user_id,int another_id);
}
