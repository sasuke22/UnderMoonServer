package com.qiqiim.webserver.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiqiim.constant.ChatEntity;
import com.qiqiim.webserver.user.dao.ChatListDao;
import com.qiqiim.webserver.user.service.ChatListService;

@Service
@Transactional
public class ChatListServiceImpl implements ChatListService{
	
	@Autowired
	ChatListDao chatListDao;

	@Override
	public List<ChatEntity> selectHistoryChat(int userId,int anotherId) {
		return chatListDao.selectHistoryChat(userId,anotherId);
	}
}
