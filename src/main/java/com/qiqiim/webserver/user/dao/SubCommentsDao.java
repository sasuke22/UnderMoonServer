package com.qiqiim.webserver.user.dao;

import com.qiqiim.constant.SubComment;

import java.sql.*;
import java.util.ArrayList;

public class SubCommentsDao {
	/**
	 * 对评论进行评论
	 * @param type 对哪个进行评论，0-邀约;1-反馈;2-社区
	 */
	public static int addCommentToComment(int type,SubComment comment){
		String sql0 = "use first_mysql_test";
		String sql1 = null;
		switch(type){
			case 0:
				sql1 = "insert into meeting_sub_comments (floorid,userid,content,date,replyid,showname,she_show_name) values(?,?,?,now(),?,?,?)";
				break;
			case 1:
				sql1 = "insert into article_sub_comments (floorid,userid,content,date,replyid,showname,she_show_name) values(?,?,?,now(),?,?,?)";
				break;
			case 2:
				sql1 = "insert into circle_sub_comments (floorid,userid,content,date,replyid,showname,she_show_name) values(?,?,?,now(),?,?,?)";
				break;
		}
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		int result = 0;
		try {
			con.setAutoCommit(false);
			ps = con.prepareStatement(sql0);
			ps.execute();
			boolean need = needInsert2ThreeTable(comment,type,con);
			if (need){
				result = insert2ThreeTable(comment,type,con);
				System.out.println("three result:"+result);
				if (result <= 0)
					return result;
			}
			ps = con.prepareStatement(sql1);
			ps.setInt(1, comment.getFloorId());
			ps.setInt(2, comment.getUserId());
			ps.setString(3, comment.getContent());
			ps.setInt(4, comment.getReplyId());
			ps.setInt(5, comment.isShowName() ? 1 : 0);
			ps.setInt(6, comment.isSheShowName() ? 1 : 0);
			ps.execute();
			con.commit();
			
			result = 1;
		} catch (SQLException e) {
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			result = -1;
		} finally {
			DBPool.close(con);
		}
		return result;
	}
	
	public static ArrayList<SubComment> getSubCommentByFloorId(int type,int id,int count){
		ArrayList<SubComment> commentsList = new ArrayList<>();
		String sq0 = "use first_mysql_test";
		String sql1 = null;
		switch (type){
			case 0://邀约
				sql1 = "select d.floorid,d.userid,d.name,d.content,d.date,d.replyid,c.name as replyName,d.showname,d.she_show_name,d.vip,d.bigVip from user as c right join (select a.*,b.name,(b.vip > now()) as vip,(b.bigVip > now()) as bigVip from meeting_sub_comments a,user b where floorid = ? and a.userid = b.id order by bigVip desc,vip desc,date desc) as d on d.replyid = c.id;";
				break;
			case 1:
				sql1 = "select d.floorid,d.userid,d.name,d.content,d.date,d.replyid,c.name as replyName,d.showname,d.she_show_name,d.vip,d.bigVip from user as c right join (select a.*,b.name,(b.vip > now()) as vip,(b.bigVip > now()) as bigVip from article_sub_comments a,user b where floorid = ? and a.userid = b.id order by bigVip desc,vip desc,date desc) as d on d.replyid = c.id;";
				break;
			case 2:
				sql1 = "select d.floorid,d.userid,d.name,d.content,d.date,d.replyid,c.name as replyName,d.showname,d.she_show_name,d.vip,d.bigVip from user as c right join (select a.*,b.name,(b.vip > now()) as vip,(b.bigVip > now()) as bigVip from circle_sub_comments a,user b where floorid = ? and a.userid = b.id order by bigVip desc,vip desc,date desc) as d on d.replyid = c.id;";
				break;
		}
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, id);
//			ps.setInt(2, count);
			rs = ps.executeQuery();
			while (rs.next()) {
				SubComment comment = new SubComment();
				comment.setFloorId(rs.getInt("floorid"));
				comment.setUserId(rs.getInt("userid"));
				comment.setUserName(rs.getString("name"));
				comment.setContent(rs.getString("content"));
				comment.setDate(new Date(rs.getTimestamp("date").getTime()));
				comment.setReplyId(rs.getInt("replyid"));
				comment.setReplyName(rs.getString("replyName"));
				comment.setShowName(rs.getInt("showname") > 0);
				comment.setSheShowName(rs.getInt("she_show_name") > 0);
				comment.setIsVip(rs.getInt("vip") > 0);
				comment.setBigVip(rs.getInt("bigVip") > 0);
				commentsList.add(comment);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			DBPool.close(con);
		}
		return commentsList;
	}

	/**
	 * 判断是否需要插入到前三条子评论的表
	 * @param comment 评论内容
	 * @param type 主题类型，对哪个进行评论，0-邀约;1-反馈;2-社区
	 */
	private static boolean needInsert2ThreeTable(SubComment comment, int type, Connection con) throws SQLException{
		boolean result = false;
		String sql1 = null;
		switch (type){
			case 0:
				sql1 = "select count(*) as count from meeting_three_sub_comments where floorid = ?";
				break;
			case 1:
				sql1 = "select count(*) as count from article_three_sub_comments where floorid = ?";
				break;
			case 2:
				sql1 = "select count(*) as count from circle_three_sub_comments where floorid = ?";
				break;
		}
		PreparedStatement ps;
		ResultSet rs;
		ps = con.prepareStatement(sql1);
		ps.setInt(1, comment.getFloorId());
		rs = ps.executeQuery();
		while (rs.next()) {
			result = rs.getInt("count") < 3;
		}
		return result;
	}

	/**
	 * 插入到层主前三个评论中
	 */
	private static int insert2ThreeTable(SubComment comment, int type, Connection con){
		String sql1 = null;
		switch(type){
			case 0:
				sql1 = "insert into meeting_three_sub_comments (floorid,userid,user_name,content,date,replyid,showname) values(?,?,?,?,now(),?,?)";
				break;
			case 1:
				sql1 = "insert into article_three_sub_comments (floorid,userid,user_name,content,date,replyid,showname) values(?,?,?,?,now(),?,?)";
				break;
			case 2:
				sql1 = "insert into circle_three_sub_comments (floorid,userid,user_name,content,date,replyid,showname) values(?,?,?,?,now(),?,?)";
				break;
		}
		PreparedStatement ps;
		int result = 0;
		
		try {
			con.setAutoCommit(false);
			ps = con.prepareStatement(sql1);
			ps.setInt(1, comment.getFloorId());
			ps.setInt(2, comment.getUserId());
			ps.setString(3,comment.getUserName());
			ps.setString(4, comment.getContent());
			ps.setInt(5, comment.getReplyId());
			ps.setInt(6, comment.isShowName() ? 1 : 0);
			ps.execute();
			con.commit();
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		}
		return result;
	}
}
