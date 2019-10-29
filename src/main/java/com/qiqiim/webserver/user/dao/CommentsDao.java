package com.qiqiim.webserver.user.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.qiqiim.constant.CommentDetail;

public class CommentsDao {
	public static int addComment(boolean isMeeting,CommentDetail comment){
		String sql0 = "use first_mysql_test";
		String sql1;
		if(isMeeting)
			sql1 = "insert into meetingComments (commentid,userid,commentname,commentgender,content,showname) values(?,?,?,?,?,?)";
		else
			sql1 = "insert into articleComments (commentid,userid,commentname,commentgender,content) values(?,?,?,?,?)";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, comment.getCommentId());
			ps.setInt(2, comment.getUserId());
			ps.setString(3, comment.getCommentName());
			ps.setInt(4, comment.getCommentGender());
			ps.setString(5, comment.getCommentContent());
			if(isMeeting)
				ps.setInt(6, comment.isShow() ? 1 : 0);
			ps.execute();
			con.commit();
			
			ResultSet rs = ps.getGeneratedKeys();
			int result = 0;
			if (rs.next()) {
				result = rs.getInt(1);
			}
			DBPool.close(con);
			return result;
		} catch (SQLException e) {
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return -1;
		}
	}
	
	public static ArrayList<CommentDetail> selectCommentsByCount(boolean isMeeting,int id,int count){
		ArrayList<CommentDetail> commentsList = new ArrayList<CommentDetail>();
		String sq0 = "use first_mysql_test";
		String sql1;
		if(isMeeting)
			sql1 = "select a.*,b.showname,(b.vip > now()) as vip,(b.bigVip > now()) as bigVip from meetingComments a,user b where commentid = ? and a.userid = b.id order by bigVip desc,vip desc,date desc limit ?,?" ;
		else
			sql1 = "select a.*,b.showname,(b.vip > now()) as vip,(b.bigVip > now()) as bigVip from articleComments a,user b where commentid = ? and a.userid = b.id order by bigVip desc,vip desc,date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, id);
			ps.setInt(2, count);
			ps.setInt(3, 20);
			rs = ps.executeQuery();
			while (rs.next()) {
				CommentDetail comment = new CommentDetail();
				comment.setId(rs.getInt("id"));
				comment.setCommentId(rs.getInt("commentid"));
				comment.setUserId(rs.getInt("userid"));
				comment.setCommentName(rs.getString("commentname"));
				comment.setCommentGender(rs.getInt("commentgender"));
				comment.setCommentContent(rs.getString("content"));
				comment.setCommentDate(new Date(rs.getTimestamp("date").getTime()));
				comment.setShow(rs.getInt("showname") > 0);
				comment.setIsVip(rs.getInt("vip") > 0);
				comment.setBigVip(rs.getInt("bigVip") > 0);
				commentsList.add(comment);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
			DBPool.close(con);
		}
		DBPool.close(con);
		return commentsList;
	}
	
	public static int deleteComment(boolean isMeeting,int id){
		String sql0 = "use first_mysql_test";
		String sql1;
		if(isMeeting)
			sql1 = "delete from meetingComments where id = ?";
		else
			sql1 = "delete from articleComments where id = ?";
		Connection con = DBPool.getConnection();
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
			ps.setInt(1, id);
			ps.execute();
			con.commit();
			DBPool.close(con);
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			DBPool.close(con);
			return -1;
		}
	}

	public static int updateComplain(boolean isMeeting, CommentDetail comment) {
		String sql0 = "use first_mysql_test";
		String sql1;
		if(isMeeting)
			sql1 = "update meetingComments set iscomplained = 1 where id = ?";
		else
			sql1 = "update articleComments set iscomplained = 1 where id = ?";
		Connection con = DBPool.getConnection();
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
			ps.setInt(1, comment.id);
			ps.execute();
			con.commit();
			DBPool.close(con);
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			DBPool.close(con);
			return -1;
		}
	}

	public static ArrayList<CommentDetail> getComplainedComments(int count, boolean isMeeting) {
		ArrayList<CommentDetail> commentsList = new ArrayList<CommentDetail>();
		String sq0 = "use first_mysql_test";
		String sql1;
		if(isMeeting)
			sql1 = "select a.*,b.showname,(b.vip > now()) as vip from meetingComments a,user b where iscomplained = 1 order by date desc limit ?,?" ;
		else
			sql1 = "select a.*,b.showname,(b.vip > now()) as vip from articleComments a,user b where iscomplained = 1 order by date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, count);
			ps.setInt(2, 20);
			rs = ps.executeQuery();
			while (rs.next()) {
				CommentDetail comment = new CommentDetail();
				comment.setId(rs.getInt("id"));
				comment.setCommentId(rs.getInt("commentid"));
				comment.setUserId(rs.getInt("userid"));
				comment.setCommentName(rs.getString("commentname"));
				comment.setCommentGender(rs.getInt("commentgender"));
				comment.setCommentContent(rs.getString("content"));
				comment.setCommentDate(new Date(rs.getTimestamp("date").getTime()));
				comment.setShow(rs.getInt("showname") > 0);
				comment.setIsVip(rs.getInt("vip") > 0);
				commentsList.add(comment);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
			DBPool.close(con);
		}
		DBPool.close(con);
		return commentsList;
	}
}
