package com.qiqiim.webserver.user.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.qiqiim.constant.ChatEntity;
/**
 * 对保存信息表的操作
 */
public interface ChatListDao {
	/**
	 * 插入消息
	 */
	public int insertChat(ChatEntity chat);
	
	/**
	 * 查询所有的离线消息
	 * 
	 */
	public ArrayList<ChatEntity> selectHistoryChat(@Param("userId")int id,@Param("anotherId")int anotherId);
}
