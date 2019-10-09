package com.qiqiim.webserver.user.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.qiqiim.constant.MeetingDetail;

public class ContributesDao {
	public static ArrayList<MeetingDetail> selectContrbutesOrderByComments(int userId,int count){
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select a.*,(b.vip > now()) as vip from meetings a,user b " +
			      "where a.approve = 1 and a.id = b.id order by top desc,commentcount desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, count);
			ps.setInt(2, count + 20);
			rs = ps.executeQuery();
			while (rs.next()) {
				MeetingDetail meetingDetail = new MeetingDetail();
				meetingDetail.setMeetingId(rs.getInt("meetingId"));
				meetingDetail.setId(rs.getInt("id"));
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setSummary(rs.getString("summary"));
				meetingDetail.setContent(rs.getString("content"));
				meetingDetail.setDate(new Date(rs.getTimestamp("date").getTime()));
				meetingDetail.setCommentCount(rs.getInt("commentcount"));
				meetingDetail.setGender(rs.getInt("gender"));
				meetingDetail.setApprove(rs.getInt("approve"));
				meetingDetail.setTop(rs.getInt("top"));
				meetingDetail.setVip(rs.getInt("vip") > 0);
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
			DBPool.close(con);
		}
		DBPool.close(con);
		return contributesList;
	}
	
	public static ArrayList<MeetingDetail> selectContrbutesOrderByDate(int userId,int count){
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select a.*,(b.vip > now()) as vip from meetings a,user b " +
			      "where approve = 1 and a.id = b.id order by top desc,date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, count);
			ps.setInt(2, count + 20);
			System.out.println(ps.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				MeetingDetail meetingDetail = new MeetingDetail();
				meetingDetail.setMeetingId(rs.getInt("meetingId"));
				meetingDetail.setId(rs.getInt("id"));
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setSummary(rs.getString("summary"));
				meetingDetail.setContent(rs.getString("content"));
				meetingDetail.setDate(new Date(rs.getTimestamp("date").getTime()));
				meetingDetail.setCommentCount(rs.getInt("commentcount"));
				meetingDetail.setGender(rs.getInt("gender"));
				meetingDetail.setApprove(rs.getInt("approve"));
				meetingDetail.setTop(rs.getInt("top"));
				meetingDetail.setVip(rs.getInt("vip") > 0);
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
		return contributesList;
	}

	public static ArrayList<MeetingDetail> selectWomanContrbutes(int userId,
			int count) {
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select a.*,(b.vip > now()) as vip from meetings a,user b " +
			      "where a.gender = 0 and a.approve = 1 and a.id = b.id order by top desc,date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, count);
			ps.setInt(2, count + 20);
			rs = ps.executeQuery();
			while (rs.next()) {
				MeetingDetail meetingDetail = new MeetingDetail();
				meetingDetail.setMeetingId(rs.getInt("meetingId"));
				meetingDetail.setId(rs.getInt("id"));
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setSummary(rs.getString("summary"));
				meetingDetail.setContent(rs.getString("content"));
				meetingDetail.setDate(new Date(rs.getTimestamp("date").getTime()));
				meetingDetail.setCommentCount(rs.getInt("commentcount"));
				meetingDetail.setGender(rs.getInt("gender"));
				meetingDetail.setApprove(rs.getInt("approve"));
				meetingDetail.setTop(rs.getInt("top"));
				meetingDetail.setVip(rs.getInt("vip") > 0);
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			e.printStackTrace();
			DBPool.close(con);
		}
		DBPool.close(con);
		return contributesList;
	}

	public static ArrayList<MeetingDetail> selectManContrbutes(int userId,
			int count) {
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select a.*,(b.vip > now()) as vip from meetings a,user b " +
			      "where a.gender = 1 and a.approve = 1 and a.id = b.id order by top desc,date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, count);
			ps.setInt(2, count + 20);
			rs = ps.executeQuery();
			while (rs.next()) {
				MeetingDetail meetingDetail = new MeetingDetail();
				meetingDetail.setMeetingId(rs.getInt("meetingId"));
				meetingDetail.setId(rs.getInt("id"));
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setSummary(rs.getString("summary"));
				meetingDetail.setContent(rs.getString("content"));
				meetingDetail.setDate(new Date(rs.getTimestamp("date").getTime()));
				meetingDetail.setCommentCount(rs.getInt("commentcount"));
				meetingDetail.setGender(rs.getInt("gender"));
				meetingDetail.setApprove(rs.getInt("approve"));
				meetingDetail.setTop(rs.getInt("top"));
				meetingDetail.setVip(rs.getInt("vip") > 0);
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			e.printStackTrace();
			DBPool.close(con);
		}
		DBPool.close(con);
		return contributesList;
	}
	
	public static MeetingDetail getInvitationDetailById(int meetingId){
		String sq0 = "use first_mysql_test";
		String sql1 = "select a.*,(b.vip > now()) as vip from meetings a,user b where meetingId = ? and a.id = b.id" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		MeetingDetail meetingDetail = new MeetingDetail();
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, meetingId);
			rs = ps.executeQuery();
			while (rs.next()) {
				meetingDetail.setId(rs.getInt("id"));
				meetingDetail.setMeetingId(rs.getInt("meetingid"));
				meetingDetail.setSummary(rs.getString("summary"));
				meetingDetail.setAge(rs.getInt("age"));
				meetingDetail.setMarry(rs.getString("marry"));
				meetingDetail.setHeight(rs.getInt("height"));
				meetingDetail.setJob(rs.getString("job"));
				meetingDetail.setGender(rs.getInt("gender"));
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setDate(new Date(rs.getTimestamp("date").getTime()));
				meetingDetail.setContent(rs.getString("content"));
				meetingDetail.setXingzuo(rs.getString("xingzuo"));
				meetingDetail.setCommentCount(rs.getInt("commentcount"));
				meetingDetail.setPics(rs.getInt("photos"));
				meetingDetail.setApprove(rs.getInt("approve"));
				meetingDetail.setReason(rs.getString("reason"));
				meetingDetail.setTop(rs.getInt("top"));
				meetingDetail.setVip(rs.getInt("vip") > 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			DBPool.close(con);
			return null;
		}
		DBPool.close(con);
		return meetingDetail;
	}
	
	public static int addContribute(MeetingDetail meetingDetail,int pics){
		String sql0 = "use first_mysql_test";
		String sql1= "insert into meetings(id,city,summary,age,marry,height,"
				+ "job,xingzuo,content,photos,gender) " +
				"values(?,?,?,?,?,?,?,?,?,?,?)";
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
			ps.setInt(1, meetingDetail.id);
			ps.setString(2, meetingDetail.city);
			ps.setString(3, meetingDetail.summary);
			ps.setInt(4, meetingDetail.age);
			ps.setString(5, meetingDetail.marry);
			ps.setInt(6, meetingDetail.height);
			ps.setString(7, meetingDetail.job);
			ps.setString(8, meetingDetail.xingzuo);
			ps.setString(9, meetingDetail.content);
			ps.setInt(10, pics);
			ps.setInt(11, meetingDetail.gender);
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
				DBPool.close(con);
			}
			e.printStackTrace();
			DBPool.close(con);
			return -1;
		}
	}
	
	public static ArrayList<MeetingDetail> getMyContributes(int userId,int count){
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * from meetings " +
			      "where id = ? order by top desc,date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, userId);
			ps.setInt(2, count);
			ps.setInt(3, count + 20);
			rs = ps.executeQuery();
			while (rs.next()) {
				MeetingDetail meetingDetail = new MeetingDetail();
				meetingDetail.setMeetingId(rs.getInt("meetingid"));
				meetingDetail.setId(rs.getInt("id"));//?
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setSummary(rs.getString("summary"));
				meetingDetail.setContent(rs.getString("content"));
				meetingDetail.setCommentCount(rs.getInt("commentcount"));
				meetingDetail.setDate(new Date(rs.getTimestamp("date").getTime()));
				meetingDetail.setGender(rs.getInt("gender"));
				meetingDetail.setApprove(rs.getInt("approve"));
				meetingDetail.setReason(rs.getString("reason"));
				meetingDetail.setTop(rs.getInt("top"));
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			DBPool.close(con);
		}
		DBPool.close(con);
		return contributesList;
	}

	public static ArrayList<MeetingDetail> getMyEnlistMeetings(ArrayList<String> enlist,int count) {
		if (enlist == null || enlist.size() <= 0) {
			return new ArrayList<MeetingDetail>();
		}
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		StringBuilder idBuilder = new StringBuilder();
		for (int i = 0;i < enlist.size();i++) {
			if (i != enlist.size()-1) 
				idBuilder.append(enlist.get(i)).append(",");
			else
				idBuilder.append(enlist.get(i));
		}
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from meetings " +
			      "where meetingid in (" + idBuilder + ") order by top desc limit ?,?";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, count);
			ps.setInt(2, count + 20);
			rs = ps.executeQuery();
			while (rs.next()) {
				MeetingDetail meetingDetail = new MeetingDetail();
				meetingDetail.setMeetingId(rs.getInt("meetingid"));
				meetingDetail.setId(rs.getInt("id"));//用户id
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setSummary(rs.getString("summary"));
				meetingDetail.setContent(rs.getString("content"));
				meetingDetail.setDate(new Date(rs.getTimestamp("date").getTime()));
				meetingDetail.setCommentCount(rs.getInt("commentcount"));
				meetingDetail.setGender(rs.getInt("gender"));
				meetingDetail.setApprove(rs.getInt("approve"));
				meetingDetail.setTop(rs.getInt("top"));
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			System.out.println("get myenilst " + e.getMessage().toString());
		}finally{
			DBPool.close(con);
		}
		return contributesList;
	}
	
	public static ArrayList<MeetingDetail> selectAllContrbutesByOldCount(int oldCount){
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * from meetings " +
			      "order by top desc,date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, oldCount);
			ps.setInt(2, oldCount + 20);
			rs = ps.executeQuery();
			while (rs.next()) {
				MeetingDetail meetingDetail = new MeetingDetail();
				meetingDetail.setMeetingId(rs.getInt("meetingid"));
				meetingDetail.setId(rs.getInt("id"));//?
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setSummary(rs.getString("summary"));
				meetingDetail.setContent(rs.getString("content"));
				meetingDetail.setDate(new Date(rs.getTimestamp("date").getTime()));
				meetingDetail.setGender(rs.getInt("gender"));
				meetingDetail.setApprove(rs.getInt("approve"));
				meetingDetail.setCommentCount(rs.getInt("commentcount"));
				meetingDetail.setTop(rs.getInt("top"));
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
			
		}finally{
			DBPool.close(con);
		}
		return contributesList;
	}
	
	public static ArrayList<MeetingDetail> selectUnapprovedContrbutesByOldCount(int oldCount){
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * from meetings where approve = 0 " +
			      "order by top desc,date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, oldCount);
			ps.setInt(2, oldCount + 20);
			rs = ps.executeQuery();
			while (rs.next()) {
				MeetingDetail meetingDetail = new MeetingDetail();
				meetingDetail.setMeetingId(rs.getInt("meetingid"));
				meetingDetail.setId(rs.getInt("id"));//?
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setSummary(rs.getString("summary"));
				meetingDetail.setContent(rs.getString("content"));
				meetingDetail.setDate(new Date(rs.getTimestamp("date").getTime()));
				meetingDetail.setGender(rs.getInt("gender"));
				meetingDetail.setApprove(rs.getInt("approve"));
				meetingDetail.setCommentCount(rs.getInt("commentcount"));
				meetingDetail.setTop(rs.getInt("top"));
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
		}finally{
			DBPool.close(con);
		}
		return contributesList;
	}

	public static int changeMeetingApprove(int meetingId, int approve, String reason) {
		String sql0 = "use first_mysql_test";
		String sql1;
		int res;
		if(approve == 1)
			sql1 = "update meetings SET approve = ?, reason = ? where meetingid = ? ";
		else 
			sql1 = "update meetings a,user b set a.approve = ?,a.reason = ?,b.score = b.score + 10 where a.meetingid = ? and a.id = b.id";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try{
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, approve);
			ps.setString(2, reason);
			ps.setInt(3, meetingId);
			ps.executeUpdate();
			con.commit();
			res = 1;
		}catch (SQLException e) {
			e.printStackTrace();
			res = 0;
		}finally{
			DBPool.close(con);
		}
		return res;
	}

	public static int reduceCommentCount(int commentId) {
		String sql0 = "use first_mysql_test";
		String sql1 = "update meetings SET commentcount = commentcount - 1 where meetingid = ? ";
		Connection con = DBPool.getConnection();
		int res;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try{
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, commentId);
			ps.executeUpdate();
			con.commit();
			res = 1;
		}catch (SQLException e) {
			e.printStackTrace();
			res = -1;
		}finally{
			DBPool.close(con);
		}
		return res;
	}
	
	public static int addCommentCount(int commentId) {
		String sql0 = "use first_mysql_test";
		String sql1 = "update meetings SET commentcount = commentcount + 1 where meetingid = ? ";
		Connection con = DBPool.getConnection();
		int res;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try{
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, commentId);
			ps.executeUpdate();
			con.commit();
			res = 1;
		}catch (SQLException e) {
			e.printStackTrace();
			res = -1;
		}finally{
			DBPool.close(con);
		}
		return res;
	}

	public static int deleteMeeting(int id){
		String sql0 = "use first_mysql_test";
		String sql1= "delete from meetings where meetingid = ?";
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
			ps.setInt(1, id);
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

	public static int topMeeting(int meetingid, int top) {
		String sql0 = "use first_mysql_test";
		String sql1 = "update meetings SET top = ? where meetingid = ? ";
		Connection con = DBPool.getConnection();
		int res;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try{
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, top);
			ps.setInt(2, meetingid);
			ps.executeUpdate();
			con.commit();
			res = 1;
		}catch (SQLException e) {
			e.printStackTrace();
			res = -1;
		}finally{
			DBPool.close(con);
		}
		return res;
	}
}
