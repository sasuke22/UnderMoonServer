package com.test.jwj.underMoon.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.test.jwj.underMoon.bean.ChatEntity;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.TranObjectType;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.*;
/**
 * 对保存信息表的操作
 * 
 *          
 */
public class SaveMsgDao {
	private static HashMap<Integer,Result> idResult = new HashMap<Integer, Result>();
	private static HashMap<Result,Integer> resultId = new HashMap<Result, Integer>();
	private static HashMap<Integer,TranObjectType> idTrantype = new HashMap<Integer, TranObjectType>();
	private static HashMap<TranObjectType,Integer> trantypeId = new HashMap<TranObjectType, Integer>(); 
	private static DBPool poolImpl = PoolManager.getInstance();
	
	static{
		//为Result枚举添加映射
		idResult.put(0, null);
		resultId.put(null, 0);
		idResult.put(1, Result.FRIEND_REQUEST_RESPONSE_ACCEPT);
		resultId.put(Result.FRIEND_REQUEST_RESPONSE_ACCEPT, 1);
		idResult.put(2, Result.FRIEND_REQUEST_RESPONSE_REJECT);
		resultId.put(Result.FRIEND_REQUEST_RESPONSE_REJECT, 2);
		idResult.put(3, Result.MAKE_FRIEND_REQUEST);
		resultId.put(Result.MAKE_FRIEND_REQUEST, 3);
		//为TranObjectType枚举添加映射
		idTrantype.put(0, null);
		trantypeId.put(null, 0);
		idTrantype.put(1, TranObjectType.FRIEND_REQUEST);
		trantypeId.put(TranObjectType.FRIEND_REQUEST, 1);
		idTrantype.put(2, TranObjectType.MESSAGE);
		trantypeId.put(TranObjectType.MESSAGE, 2);
	}
	private SaveMsgDao(){
		
	}

	
	/**
	 * 插入消息
	 * 
	 */
	public static void insertSaveMsg(int myid,TranObject tran){		
		String sql0 = "use first_mysql_test";
		String sql1= "insert into SaveMsg(sendid,getid,msg,trantype,time,resultType,messageType,sendname)" +
				"values(?,?,?,?,?,?,?,?)";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String msg = "";
		int messageType = ChatEntity.RECEIVE;
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, myid);
			ps.setInt(2, tran.getReceiveId());
			//message信息，时间来自ChatEntity对象，否则在tran中
			if(tran.getTranType() == TranObjectType.MESSAGE){
				ChatEntity chatEntity = (ChatEntity)tran.getObject();
				msg = chatEntity.getContent();
				messageType = chatEntity.getMessageType();
				ps.setString(5, chatEntity.getSendTime());
			}
			else
				ps.setString(5, tran.getSendTime());
			ps.setString(3, msg);
			ps.setInt(4, trantypeId.get(tran.getTranType()));
			
			ps.setInt(6, resultId.get(tran.getResult()));
			ps.setInt(7, messageType);
			ps.setString(8, tran.getSendName());
			ps.execute();
			con.commit();
			poolcon.close();
		} catch (SQLException e) {
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			poolcon.close();
		}
	}
	/**
	 * 删除保存的离线信息
	 */
	public static void  deleteSaveMsg(int getid){
		String sql0 = "use first_mysql_test";
		String sql1 = "delete from saveMsg " +
				      "where getid=?";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		PreparedStatement ps;
		try {
			con.setAutoCommit(false);
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, getid);
			ps.execute();
			con.commit();
			poolcon.close();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			poolcon.close();
		}
	}
	/**
	 * 查询所有的离线消息
	 * 
	 */
	public static ArrayList<TranObject> selectMsg(int id) {
		ArrayList<TranObject> msgList = new ArrayList<TranObject>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
				      "from saveMsg " +
				      "where getid=?";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		PreparedStatement ps ;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while(rs.next()){
				TranObject tran = new TranObject();
				tran.setSendId(rs.getInt("sendid"));
				tran.setTranType(idTrantype.get(rs.getInt("trantype")));
				tran.setSendName(rs.getString("sendname"));
				tran.setResult(idResult.get(rs.getInt("resultType")));
				if(idTrantype.get(rs.getInt("trantype"))==TranObjectType.MESSAGE){
					ChatEntity chatEntity = new ChatEntity();
					chatEntity.setContent(rs.getString("msg"));
					chatEntity.setMessageType(rs.getInt("messageType"));
					chatEntity.setReceiverId(tran.getReceiveId());
					chatEntity.setSenderId(tran.getSendId());
					chatEntity.setSendTime(rs.getString("time"));
					tran.setObject(chatEntity);
				}else if(idResult.get(rs.getInt("resultType"))==Result.FRIEND_REQUEST_RESPONSE_ACCEPT){
					ArrayList<User> list = UserDao.selectFriendByAccountOrID(tran.getSendId());
					tran.setObject(list.get(0));
					tran.setSendTime(rs.getString("time"));
				}else{
					tran.setSendTime(rs.getString("time"));
				}
				msgList.add(tran);
			}
			
		} catch (SQLException e) {
			poolcon.close();
			e.printStackTrace();
		}
		poolcon.close();
		return msgList;
	}
}
