package com.qiqiim.webserver.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.qiqiim.constant.Complain;

public class ComplainDao {
	public static List<Complain> selectComplain(int count){
		ArrayList<Complain> complainList = new ArrayList<Complain>();
		String sql1 = "select * from complain order by time desc limit ?,20" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql1);
			ps.setInt(1, count);
			rs = ps.executeQuery();
			while (rs.next()) {
				Complain complain = new Complain();
				complain.setId(rs.getInt("id"));
				complain.setUserid(rs.getInt("userId"));
				complain.setComplainid(rs.getInt("complainId"));
				complain.setPics(rs.getInt("pics"));
				complain.setRemark(rs.getString("remark"));
				complainList.add(complain);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
		return complainList;
	}
	
	public static int addComplain(Complain complain,int pics){
		int res = 0;
		String sql0 = "use first_mysql_test";
		String sql1= "insert into complain (userId,complainId,pics,remark,type) " +
				"values(?,?,?,?,?)";
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
			ps.setInt(1, complain.getUserid());
			ps.setInt(2, complain.getComplainid());
			ps.setInt(3, complain.getPics());
			ps.setString(4, complain.getRemark());
			ps.setString(5, complain.getType());
			ps.execute();
			con.commit();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				res = rs.getInt(1);
			}
		} catch (SQLException e) {
			res = -1;
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				DBPool.close(con);
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			DBPool.close(con);
		}
		return res;
	}
}
