package com.test.jwj.underMoon.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.test.jwj.underMoon.bean.MeetingDetail;
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
}
