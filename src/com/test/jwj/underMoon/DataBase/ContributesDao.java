package com.test.jwj.underMoon.DataBase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.Result;
public class ContributesDao {
	public static ArrayList<MeetingDetail> selectContrbutesById(int userId){
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from meetings " +
			      "where id not in (?)" ;
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
	
	public static MeetingDetail getInvitationDetailById(TranObject tran){
		int meetingId = (Integer)tran.getObject();
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
			ArrayList<String> enlisters = queryRegistName(tran);
			meetingDetail.setEnlistersName(enlisters);
		} catch (Exception e) {
			return null;
		}
		DBPool.close(con);
		return meetingDetail;
	}
	
	public static int addContribute(TranObject tran){
		String sql0 = "use first_mysql_test";
		String sql1= "insert into meetings(id,city,summary,date,type,lovetype,age,marry,height,"
				+ "job,figure,xingzuo,content)" +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
			ps.setString(5, meetingDetail.type);
			ps.setString(6, meetingDetail.loveType);
			ps.setInt(7, meetingDetail.age);
			ps.setInt(8, meetingDetail.marry);
			ps.setInt(9, meetingDetail.height);
			ps.setString(10, meetingDetail.job);
			ps.setString(11, meetingDetail.figure);
			ps.setString(12, meetingDetail.xingzuo);
			ps.setString(13, meetingDetail.content);
			ps.execute();
			con.commit();
			return 1;
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
	
	public static Result addEnlist(TranObject tran){
		String sql0 = "use first_mysql_test";
		String sql1 = "update meetings set backid= case when isnull(backid) or backid='' then ? else concat(backid,'|',?) end where meetingId =?";
		String sql2 = "update meetings set backName= case when isnull(backName) or backName='' then ? else concat(backName,'|',?) end where meetingId =?";
		int userId = tran.getSendId();
		int meetingId = (Integer)tran.getObject();
		String enlisterName = tran.getSendName();
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
			ps.setInt(1, userId);
			ps.setInt(2, userId);
			ps.setInt(3, meetingId);
			System.out.println(ps.toString());
			ps.execute();
			ps = con.prepareStatement(sql2);
			ps.setString(1, enlisterName);
			ps.setString(2, enlisterName);
			ps.setInt(3, meetingId);
			System.out.println(ps.toString());
			ps.execute();
			con.commit();
			return Result.ENLIST_SUCCESS;
		}catch (Exception e){
			return Result.ENLIST_FAILED;
		}
	}
	
	public static ArrayList<MeetingDetail> getMyContributes(int userId){
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

	public static ArrayList<MeetingDetail> getMyEnlistMeetings(ArrayList<String> enlist) {
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
			      "where id in (" + idBuilder + ")";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		//TODO 还没弄完
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
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
	
	public static ArrayList<String> queryRegistName(TranObject tran){
		String sql0 = "use first_mysql_test";
		String sql1 = "select backName from meetings where meetingId = ?";
		ArrayList<String> backNameList = new ArrayList<String>();
		int meetingId = (Integer)tran.getObject();
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, meetingId);
			System.out.println(ps.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				String id = rs.getString("backName");
				String[] idArray = id.split("|");
				for (String string : idArray) {
					if (!string.equalsIgnoreCase("|")) {
						backNameList.add(string);
					}
				}
			}
			return backNameList;
		}catch (Exception e){
			return null;
		}
	}
}
