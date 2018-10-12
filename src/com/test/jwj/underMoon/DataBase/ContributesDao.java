package com.test.jwj.underMoon.DataBase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.global.Result;
public class ContributesDao {
	private static DBPool poolImpl = PoolManager.getInstance();
	public static ArrayList<MeetingDetail> selectContrbutesById(int userId){
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from meetings " +
			      "where id not in (?)" ;
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
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
				meetingDetail.setDate(rs.getDate("date"));
//				meetingDetail.setRead(rs.getBoolean("read"));
				meetingDetail.setApprove(rs.getBoolean("approve"));
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
		}
		poolcon.close();
		return contributesList;
	}
	
	public static ArrayList<MeetingDetail> selectContrbutesByDate(int userId,Date curDate){
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from meetings " +
			      "where date = ?" ;
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
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
				meetingDetail.setDate(rs.getDate("date"));
//				meetingDetail.setRead(rs.getBoolean("read"));
				meetingDetail.setApprove(rs.getBoolean("approve"));
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			
		}
		poolcon.close();
		return contributesList;
	}
	
	public static MeetingDetail getInvitationDetailById(TranObject tran){
		int meetingId = (Integer)tran.getObject();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from meetings " +
			      "where meetingId = ?" ;
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
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
				meetingDetail.setType(rs.getString("type"));
				meetingDetail.setLoveType(rs.getString("lovetype"));
				meetingDetail.setAge(rs.getInt("age"));
				meetingDetail.setMarry(rs.getInt("marry"));
				meetingDetail.setHeight(rs.getInt("height"));
				meetingDetail.setJob(rs.getString("job"));
				meetingDetail.setFigure(rs.getString("figure"));
				meetingDetail.setCity(rs.getString("city"));
				meetingDetail.setDate(rs.getDate("date"));
				meetingDetail.setContent(rs.getString("content"));
				meetingDetail.setXingzuo(rs.getString("xingzuo"));
				meetingDetail.setEnlistersName(rs.getString("backName"));
				meetingDetail.setRegistId(rs.getString("backId"));
			}
//			HashMap<String,String> enlisters = queryRegistName(tran);
//			if (enlisters != null) {
//				ArrayList<String> enlisterNames = new ArrayList<String>();
//				for (Map.Entry<String, String> entry : enlisters.entrySet()) {
//					enlisterNames.add(entry.getValue());
//				}
//				meetingDetail.setEnlistersName(enlisterNames);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			poolcon.close();
			return null;
		}
		poolcon.close();
		return meetingDetail;
	}
	
	public static int addContribute(TranObject tran){
		String sql0 = "use first_mysql_test";
		String sql1= "insert into meetings(id,city,summary,date,type,lovetype,age,marry,height,"
				+ "job,figure,xingzuo,content)" +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
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
			ps.setDate(4, meetingDetail.date);
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
			poolcon.close();
			int res = UserDao.updateScore(meetingDetail.id, meetingDetail.score - 100);
			return res;
		} catch (SQLException e) {
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			poolcon.close();
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
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
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
			poolcon.close();
			return Result.ENLIST_SUCCESS;
		}catch (Exception e){
			poolcon.close();
			return Result.ENLIST_FAILED;
		}
	}
	
	public static ArrayList<MeetingDetail> getMyContributes(int userId){
		ArrayList<MeetingDetail> contributesList = new ArrayList<MeetingDetail>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from meetings " +
			      "where id = ?" ;
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
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
				meetingDetail.setDate(rs.getDate("date"));
//				meetingDetail.setRead(rs.getBoolean("read"));
				meetingDetail.setApprove(rs.getBoolean("approve"));
				contributesList.add(meetingDetail);
			}
		} catch (Exception e) {
			
		}
		poolcon.close();
		return contributesList;
	}

	public static ArrayList<MeetingDetail> getMyEnlistMeetings(ArrayList<String> enlist) {
		if (enlist.size() <= 0) {
			return null;
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
			      "where id in (" + idBuilder + ")";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
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
				meetingDetail.setDate(rs.getDate("date"));
//				meetingDetail.setRead(rs.getBoolean("read"));
				meetingDetail.setApprove(rs.getBoolean("approve"));
				contributesList.add(meetingDetail);
			}
			
		} catch (Exception e) {
			System.out.println("get myenilst " + e.getMessage().toString());
		}
		poolcon.close();
		return contributesList;
	}
	
	public static HashMap<String,String> queryRegistName(TranObject tran){
		String sql0 = "use first_mysql_test";
		String sql1 = "select backId,backName from meetings where meetingId = ?";
		HashMap<String,String> backNameList = new HashMap<String,String>();
		int meetingId = (Integer)tran.getObject();
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
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
				String id = rs.getString("backid");
				String name = rs.getString("backName");
				if (id == null || name == null || id.equals("") || name.equals("")) {
					return null;
				}
				String[] idArray = id.split("\\|");
				String[] nameArray = name.split("\\|");
				for (int i = 0;i < idArray.length;i++) {
					backNameList.put(idArray[i], nameArray[i]);
				}
			}
			poolcon.close();
			return backNameList;
		}catch (Exception e){
			e.printStackTrace();
			poolcon.close();
			return null;
		}
	}
	
	public static String getMeetingPhotosAddress(int meetingId) {
		String sql0 = "use first_mysql_test";
		String sql1 = "select photos from user where meetingid = ?";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		ResultSet rs;
		String id = null;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, meetingId);
			System.out.println(ps.toString());
			rs = ps.executeQuery();
			if (rs.first())
				id = rs.getString("photos");
			poolcon.close();
			return id;
		}catch (Exception e){
			System.out.println("select regist " + e.getMessage().toString());
			poolcon.close();
			return null;
		}
	}
	
	public static Result updateMeetingPhotos(int meetingId,int lastPhoto){
		String sql0 = "use first_mysql_test";
		String sql1 = "update user set photos= case when isnull(photos) or photos='' then ? else concat(photos,'|',?) end where meetingid =?";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
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
			ps.setInt(1, lastPhoto);
			ps.setInt(2, lastPhoto);
			ps.setInt(3, meetingId);
			System.out.println(ps.toString());
			ps.execute();
			con.commit();
			poolcon.close();
			return Result.ENLIST_SUCCESS;
		}catch (Exception e){
			poolcon.close();
			return Result.ENLIST_FAILED;
		}
	}
}
