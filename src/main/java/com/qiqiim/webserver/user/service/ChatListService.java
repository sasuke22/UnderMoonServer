package com.qiqiim.webserver.user.service;

import java.util.List;

import com.qiqiim.constant.ChatEntity;

public interface ChatListService {
	List<ChatEntity> selectHistoryMsg(int userId);
	
	int insertMsg(ChatEntity chat);
}
