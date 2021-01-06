package com.qiqiim.webserver.user.dao;

import java.net.URLDecoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.qiqiim.constant.ChatEntity;

public class NewChatListDao {
	public static List<ChatEntity> selectHistoryChat(int userId,int anotherId){
		ArrayList<ChatEntity> chatList = new ArrayList<ChatEntity>();
		String sql1 = "select * from messages where userId = ? and anotherId = ? or userId = ? and anotherId = ? order by time desc" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql1);
			ps.setInt(1, userId);
			ps.setInt(2, anotherId);
			ps.setInt(3, anotherId);
			ps.setInt(4, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				ChatEntity chat = new ChatEntity();
				chat.setUserId(rs.getInt("userId"));
				chat.setAnotherId(rs.getInt("anotherId"));
				chat.setTime(new Date(rs.getTimestamp("time").getTime()));
				chat.setContent(URLDecoder.decode(rs.getString("content"),"utf-8"));
				chat.setType(rs.getInt("type"));
				chatList.add(chat);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
		return chatList;
	}

	public static int deleteMessage(int messageId) {
		String sql0 = "use first_mysql_test";
		String sql1= "delete from messages where id = ?";
		Connection con = DBPool.getConnection();
		int res;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, messageId);
			ps.execute();
			con.commit();
			res = 1;
		} catch (SQLException e) {
			System.out.println("正在回滚");
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
}
