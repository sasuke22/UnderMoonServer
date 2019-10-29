package com.qiqiim.webserver.user.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qiqiim.constant.Message;
import com.qiqiim.webserver.base.dao.BaseDao;

/**
 * 用户帐号
 * 
 * @author qiqiim
 * @email 1044053532@qq.com
 * @date 2017-11-27 09:38:52
 */
public interface MsgListDao extends BaseDao<List<Message>> {
	public List<Message> queryMessageList(int user_id);
	
	public void insertMessage(@Param("userId")int user_id, @Param("anotherId")int another_id,@Param("content")String content);

	public void deleteMessage(@Param("userId")int user_id, @Param("anotherId")int another_id);
	
	public void readMessage(@Param("userId")int user_id, @Param("anotherId")int another_id);

	public List<Integer> queryIfUnread(int userId);
}
