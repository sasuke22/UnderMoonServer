package com.qiqiim.webserver.user.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang.time.DateUtils;

public class CountDao {
	/**
	 * 查询是否到达每日陌生人聊天次数
	 * @param userId 用户id
	 * @param sheId 接收方id
	 * @return 1-正常;2-到达10次限制;-1-发生异常
	 */
	public static int addTalkCount(int userId, int sheId){
		String sq0 = "use first_mysql_test";
		String sql0 = "select * from messages where userId = ? and anotherId = ? or anotherId = ? and userId = ? limit 1";
		String sql1 = "select * from daily_count where id = ?";
		String sql2;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			try {
				con.setAutoCommit(false);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			ps.execute();
			ps = con.prepareStatement(sql0);
			ps.setInt(1, userId);
			ps.setInt(2, sheId);
			ps.setInt(3, sheId);
			ps.setInt(4, userId);
			rs = ps.executeQuery();
			if (rs.next()){//证明之前有聊过天，不用更新次数
				return 1;
			}
			ps = con.prepareStatement(sql1);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {//证明有插入，先看时间，更新时间，时间一致就更新次数
				int count = rs.getInt("count");
				Date date = rs.getDate("date");
				Date now = new Date(System.currentTimeMillis());
				if (!DateUtils.isSameDay(date, now)){//更新次数
					sql2 = "update daily_count set count = count + 1 where id = ?";
				} else {
					if (count < 10)
						sql2 = "update daily_count set count = 1,date = now() where id = ?";
					else
						return 2;
				}
			} else {//数据中没有插入
				sql2 = "insert daily_count (id,count,date) values (?,1,now())";
			}
			ps = con.prepareStatement(sql2);
			ps.setInt(1, userId);
			ps.execute();
			con.commit();
			return 1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
}
