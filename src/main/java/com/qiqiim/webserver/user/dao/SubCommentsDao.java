package com.qiqiim.webserver.user.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.qiqiim.constant.CommentDetail;
import com.qiqiim.constant.SubComment;

public class SubCommentsDao {
	/**
	 * 对评论进行评论
	 * @param type 对哪个进行评论，0-邀约;1-反馈;2-社区
	 * @param comment
	 * @return
	 */
	public static int addCommentToComment(int type,SubComment comment){
		String sql0 = "use first_mysql_test";
		String sql1 = null;
		switch(type){
			case 0:
				sql1 = "insert into meeting_sub_comments (floorid,userid,content,date,replyid,showname) values(?,?,?,now(),?,?)";
				break;
			case 1:
				sql1 = "insert into article_sub_comments (floorid,userid,content,date,replyid,showname) values(?,?,?,now(),?,?)";
				break;
			case 2:
				sql1 = "insert into circle_sub_comments (floorid,userid,content,date,replyid,showname) values(?,?,?,now(),?,?)";
				break;
		}
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
			ps.setInt(1, comment.getFloorId());
			ps.setInt(2, comment.getUserId());
			ps.setString(3, comment.getContent());
			ps.setInt(4, comment.getReplyId());
			ps.setInt(5, comment.getShowName());
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
}
