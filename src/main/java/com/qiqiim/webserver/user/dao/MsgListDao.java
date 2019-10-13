package com.qiqiim.webserver.user.dao;
import java.util.List;

import com.qiqiim.constant.ChatEntity;
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
	
	public int msgExist(int user_id,int another_id);
	
	public void insertMessage(ChatEntity chat);

	public void updateMessage(ChatEntity chat);
}
