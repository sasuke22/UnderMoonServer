package com.qiqiim.webserver.user.dao;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.qiqiim.constant.ChatEntity;
import com.qiqiim.constant.Message;

public class NewMsgListDao {
	public static List<Message> queryMessageList(int userId){
		ArrayList<Message> articlesList = new ArrayList<Message>();
		String sql1 = "select a.*,b.name,(b.vip > now()) as vip,(b.bigVip > now()) as bigVip from msg_list a,user b where userId = ? and a.anotherId = b.id order by time desc" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql1);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				Message article = new Message();
				article.setUserId(rs.getInt("userId"));
				article.setAnotherId(rs.getInt("anotherId"));
				article.setTime(new Date(rs.getTimestamp("time").getTime()));
				article.setContent(URLDecoder.decode(rs.getString("content"),"utf-8"));
				article.setUnread(rs.getInt("unread"));
				article.setName(rs.getString("name"));
				articlesList.add(article);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
		return articlesList;
	}
	
	public static void insertMessage(ChatEntity chat){
		String sql1 = "insert into msg_list (userId,anotherId,content,unread,time) values " +
				"(?,?,?,1,CURRENT_TIMESTAMP) on duplicate key update content=?,time=CURRENT_TIMESTAMP;";
		String sql2 = "insert into msg_list (userId,anotherId,content,unread,time) values " +
				"(?,?,?,1,CURRENT_TIMESTAMP) on duplicate key update content=?,unread=unread+1,time=CURRENT_TIMESTAMP;";
		String sql3 = "insert into messages (userId,anotherId,content,time,type) values " +
				"(?,?,?,CURRENT_TIMESTAMP,?);";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try {
			if(chat.getType() == 0){
				ps = con.prepareStatement(sql1);
				ps.setInt(1, chat.getUserId());
				ps.setInt(2, chat.getAnotherId());
				ps.setString(3, URLEncoder.encode(chat.getContent(),"utf-8"));
				ps.setString(4, URLEncoder.encode(chat.getContent(),"utf-8"));
				ps.execute();
				
				ps = con.prepareStatement(sql2);
				ps.setInt(1, chat.getAnotherId());
				ps.setInt(2, chat.getUserId());
				ps.setString(3, URLEncoder.encode(chat.getContent(),"utf-8"));
				ps.setString(4, URLEncoder.encode(chat.getContent(),"utf-8"));
				ps.execute();
			}
			
			ps = con.prepareStatement(sql3);
			ps.setInt(1, chat.getUserId());
			ps.setInt(2, chat.getAnotherId());
			ps.setString(3, URLEncoder.encode(chat.getContent(),"utf-8"));
			ps.setInt(4, chat.getType());
			ps.execute();
			con.commit();
		} catch (Exception e) {
			System.out.println("插入消息正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			DBPool.close(con);
		}
	}
	
	public static int deleteMessage(int userId,int anotherId){
		String sql1= "delete from msg_list where userId = ? and anotherId = ?";
		Connection con = DBPool.getConnection();
		int res;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql1);
			ps.setInt(1, userId);
			ps.setInt(2, anotherId);
			ps.execute();
			con.commit();
			res = 1;
		} catch (SQLException e) {
			System.out.println("删除消息列表正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			res = -1;
		}finally{
			DBPool.close(con);
		}
		return res;
	}
	
	public static void readMessage(int userId,int anotherId){
		String sql1= "update msg_list set unread = 0 where userId = ? and anotherId = ?";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql1);
			ps.setInt(1, userId);
			ps.setInt(2, anotherId);
			ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
	}
	
	public static int queryIfUnread(int userId){
		String sql1 = "select unread from msg_list where userId = ?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		int res = 0;
		try {
			ps = con.prepareStatement(sql1);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				res += rs.getInt("unread");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
		return res;
	}
}
