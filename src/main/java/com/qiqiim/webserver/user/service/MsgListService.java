package com.qiqiim.webserver.user.service;

import java.util.List;

import com.qiqiim.constant.ChatEntity;
import com.qiqiim.constant.Message;

public interface MsgListService {
	List<Message> queryMessageList(int user_id);
	
	int msgExist(int user_id,int another_id);
	
	void updateMessage(ChatEntity chat);

	void insertMessage(ChatEntity chat);
}
