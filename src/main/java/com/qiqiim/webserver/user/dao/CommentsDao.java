package com.qiqiim.webserver.user.dao;

import com.qiqiim.constant.CommentDetail;
import com.qiqiim.constant.SubComment;
import org.apache.http.util.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentsDao {
	public static int addComment(boolean isMeeting,CommentDetail comment){
		if (TextUtils.isEmpty(comment.commentContent))
			return -1;
		String sql0 = "use first_mysql_test";
		String sql1;
		if(isMeeting)
			sql1 = "insert into meetingComments (commentid,userid,commentname,commentgender,content,showname) values(?,?,?,?,?,?)";
		else
			sql1 = "insert into articleComments (commentid,userid,commentname,commentgender,content) values(?,?,?,?,?)";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		int result = 0;
		try {
			con.setAutoCommit(false);
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, comment.getCommentId());
			ps.setInt(2, comment.getUserId());
			ps.setString(3, comment.getCommentName());
			ps.setInt(4, comment.getCommentGender());
			ps.setString(5, URLEncoder.encode(comment.getCommentContent(),"utf-8"));
			if(isMeeting)
				ps.setInt(6, comment.isShow() ? 1 : 0);
			ps.execute();
			con.commit();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
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
	
	public static ArrayList<CommentDetail> selectCommentsByCount(boolean isMeeting,int id,int count){
		ArrayList<CommentDetail> commentsList = new ArrayList<>();
		String sq0 = "use first_mysql_test";
		String sql1;
		String sql2;
		// TODO 这里只显示3个子评论，导致app那边认为只有三个子评论，不显示点击查看更多评论
		if(isMeeting){
			sql1 = "select id from meetings where meetingid = " + id + " limit 1";
			sql2 = "select * from meeting_three_sub_comments as c right join (select a.*,b.vip > now() as vip,b.bigVip > now() as bigVip,a.userid = ? as self from meetingcomments as a,user as b where a.commentid = ? and a.userid = b.id order by self desc,bigVip desc,vip desc,date desc limit ?,20) as d on c.floorid = d.id";
		} else {
			sql1 = "select userId from articles where id = " + id + " limit 1";
			sql2 = "select * from article_three_sub_comments as c right join (select a.*,b.vip > now() as vip,b.bigVip > now() as bigVip,a.userid = ? as self from articlecomments as a,user as b where a.commentid = ? and a.userid = b.id order by self desc,bigVip desc,vip desc,date desc limit ?,20) as d on c.floorid = d.id";
		}

		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		int userId = 0;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			rs = ps.executeQuery();
			if(rs.next()){
				userId = rs.getInt(isMeeting ? "id" : "userId");
			}
			ps = con.prepareStatement(sql2);
			ps.setInt(1, userId);
			ps.setInt(2, id);
			ps.setInt(3, count);
			rs = ps.executeQuery();
			while (rs.next()) {
				boolean exist = false;
				SubComment subComment = new SubComment();
				if(rs.getTimestamp("c.date") != null){//证明没有子评论，直接添加
					subComment.setFloorId(rs.getInt("floorid"));
					subComment.setUserId(rs.getInt("c.userid"));
					subComment.setUserName(rs.getString("user_name"));
					subComment.setContent(URLDecoder.decode(rs.getString("c.content"),"utf-8"));
					subComment.setDate(new Date(rs.getTimestamp("c.date").getTime()));
					subComment.setReplyId(rs.getInt("replyid"));
					subComment.setReplyName(rs.getString("reply_name"));
					subComment.setShowName(rs.getInt("c.showname") > 0);
					subComment.setSheShowName(rs.getInt("she_show_name") > 0);
					for (CommentDetail commentDetail : commentsList) {
						if (commentDetail.id == rs.getInt("id")){//列表中已经有了这个评论，则只需要加载子评论
							exist = true;
							commentDetail.subComments.add(subComment);
							break;
						}
					}
				}
				
				if (!exist){//列表中没有该评论，需要将子评论和该评论一起添加
					CommentDetail comment = new CommentDetail();
					comment.setId(rs.getInt("id"));
					comment.setCommentId(rs.getInt("commentid"));
					comment.setUserId(rs.getInt("d.userid"));
					comment.setCommentName(rs.getString("commentname"));
					comment.setCommentGender(rs.getInt("commentgender"));
					comment.setCommentContent(URLDecoder.decode(rs.getString("d.content"),"utf-8"));
					comment.setCommentDate(new Date(rs.getTimestamp("d.date").getTime()));
					comment.setShow(rs.getInt("d.showname") > 0);
					comment.setIsVip(rs.getInt("vip") > 0);
					comment.setBigVip(rs.getInt("bigVip") > 0);
					List<SubComment> subList = new ArrayList<>();
					if(rs.getTimestamp("c.date") != null)
						subList.add(subComment);
					comment.setSubComments(subList);
					commentsList.add(comment);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			DBPool.close(con);
		}
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
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return -1;
		} finally {
			DBPool.close(con);
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
				comment.setCommentContent(URLDecoder.decode(rs.getString("content"),"utf-8"));
				comment.setCommentDate(new Date(rs.getTimestamp("date").getTime()));
				comment.setShow(rs.getInt("showname") > 0);
				comment.setIsVip(rs.getInt("vip") > 0);
				commentsList.add(comment);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			DBPool.close(con);
		}
		DBPool.close(con);
		return commentsList;
	}
}
