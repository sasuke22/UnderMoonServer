package com.qiqiim.webserver.user.service;

import java.util.List;

import com.qiqiim.constant.Message;

public interface MsgListService {
	List<Message> queryMessageList(int userId);
}
