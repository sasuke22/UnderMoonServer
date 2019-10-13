package com.qiqiim.webserver.user.dao;

import java.util.ArrayList;
import com.qiqiim.constant.ChatEntity;
/**
 * 对保存信息表的操作
 */
public interface ChatListDao {
	/**
	 * 插入消息
	 */
	public int insertMsg(ChatEntity chat);
	
	/**
	 * 删除保存的离线信息
	 */
	public void  deleteMsg(int userId,int receiveId);
	
	/**
	 * 查询所有的离线消息
	 * 
	 */
	public ArrayList<ChatEntity> selectHistoryMsg(int id);
}
