package com.qiqiim.webserver.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiqiim.constant.Message;
import com.qiqiim.webserver.user.dao.MsgListDao;
import com.qiqiim.webserver.user.service.MsgListService;

@Service
@Transactional
public class MsgListServiceImpl implements MsgListService{
	
	@Autowired
	MsgListDao msgListDao;

	@Override
	public List<Message> queryMessageList(int user_id) {
		return msgListDao.queryMessageList(user_id);
	}

	@Override
	public void insertMessage(int user_id,int another_id,String content) {
		msgListDao.insertMessage(user_id,another_id,content);
	}

	@Override
	public void deleteMessage(int user_id, int another_id) {
		msgListDao.deleteMessage(user_id,another_id);
	}

	@Override
	public void readMessage(int user_id, int another_id) {
		msgListDao.readMessage(user_id,another_id);
	}

	@Override
	public List<Integer> queryIfUnread(int userId) {
		return msgListDao.queryIfUnread(userId);
	}
}
