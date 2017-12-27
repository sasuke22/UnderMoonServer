package com.test.jwj.underMoon.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.TranObject;

public class ContributesDao {
	public static ArrayList<MeetingDetail> selectContrbutesById(int userId){
		ArrayList<TranObject> contributesList = new ArrayList<TranObject>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from friendlist as f left outer join user as u " +
			      "on f.friendid=u.id "+
			      "where master=?";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		
		return null;
	}
}
