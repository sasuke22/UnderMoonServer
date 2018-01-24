package com.test.jwj.underMoon.DataBase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.TranObject;
public class ContributesDao {
	public static ArrayList<MeetingDetail> selectContrbutesById(int userId){
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from meetings " +
			      "where id = ?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				MeetingDetail meetingDetail = new MeetingDetail();
				meetingDetail.setMeetingId(rs.getInt("meetingId"));
				meetingDetail.setId(rs.getInt("id"));//?
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setSummary(rs.getString("summary"));
				meetingDetail.setDate(rs.getString("date"));
				meetingDetail.setRead(rs.getBoolean("read"));
				meetingDetail.setApprove(rs.getBoolean("approve"));
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			
		}
		DBPool.close(con);
		return contributesList;
	}
	
	public static ArrayList<MeetingDetail> selectContrbutesByDate(int userId,Date curDate){
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from meetings " +
			      "where date = ?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setDate(1, curDate);
			rs = ps.executeQuery();
			while (rs.next()) {
				MeetingDetail meetingDetail = new MeetingDetail();
				meetingDetail.setMeetingId(rs.getInt("meetingId"));
				meetingDetail.setId(rs.getInt("id"));//?
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setSummary(rs.getString("summary"));
				meetingDetail.setDate(rs.getString("date"));
				meetingDetail.setRead(rs.getBoolean("read"));
				meetingDetail.setApprove(rs.getBoolean("approve"));
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			
		}
		DBPool.close(con);
		return contributesList;
	}
	
	public static MeetingDetail getInvitationDetailById(int meetingId){
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from meetings " +
			      "where meetingId = ?" ;
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
				meetingDetail.setType(rs.getString("type"));
				meetingDetail.setLoveType(rs.getString("lovetype"));
				meetingDetail.setAge(rs.getInt("age"));
				meetingDetail.setMarry(rs.getInt("marry"));
				meetingDetail.setHeight(rs.getInt("height"));
				meetingDetail.setJob(rs.getString("job"));
				meetingDetail.setFigure(rs.getString("figure"));
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setDate(rs.getString("date"));
				meetingDetail.setContent(rs.getString("content"));
				meetingDetail.setXingzuo(rs.getString("xingzuo"));
			}
		} catch (Exception e) {
			
		}
		DBPool.close(con);
		return meetingDetail;
	}
	
	public static void addContribute(TranObject tran){
		String sql0 = "use first_mysql_test";
		String sql1= "insert into SaveMsg(id,city,summary,date,read,approve,type,lovetype,age,marry,height,"
				+ "job,figure,xingzuo,content)" +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		MeetingDetail meetingDetail = (MeetingDetail) tran.getObject();
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, meetingDetail.id);
			ps.setString(2, meetingDetail.city);
			ps.setString(3, meetingDetail.summary);
			ps.setString(4, meetingDetail.date);
			ps.setBoolean(5, meetingDetail.read);
			ps.setBoolean(6, meetingDetail.approve);
			ps.setString(7, meetingDetail.type);
			ps.setString(8, meetingDetail.loveType);
			ps.setInt(9, meetingDetail.age);
			ps.setInt(10, meetingDetail.marry);
			ps.setInt(11, meetingDetail.height);
			ps.setString(12, meetingDetail.job);
			ps.setString(13, meetingDetail.figure);
			ps.setString(14, meetingDetail.xingzuo);
			ps.setString(15, meetingDetail.content);
			ps.execute();
			con.commit();
		} catch (SQLException e) {
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}
