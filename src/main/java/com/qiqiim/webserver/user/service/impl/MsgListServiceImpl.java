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
	public List<Message> queryMessageList(int userId) {
		return msgListDao.queryMessageList(userId);
	}

}
