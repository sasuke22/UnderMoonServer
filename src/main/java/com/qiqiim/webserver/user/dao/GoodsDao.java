package com.qiqiim.webserver.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.qiqiim.webserver.user.model.GoodsBean;

public class GoodsDao {
	public static ArrayList<GoodsBean> getGoodsList(){
		ArrayList<GoodsBean> GoodsList = new ArrayList<GoodsBean>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * from goods";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			rs = ps.executeQuery();
			while (rs.next()) {
				GoodsBean goods = new GoodsBean();
				goods.setGoodsId(rs.getInt("goods_id"));
				goods.setGoodsName(rs.getString("goods_name"));
				goods.setGoodsPrice(rs.getString("goods_price"));
				goods.setGoodsUrl(rs.getString("goods_url"));
				GoodsList.add(goods);
			}
		} catch (Exception e) {
			e.printStackTrace();
			DBPool.close(con);
		}
		DBPool.close(con);
		return GoodsList;
	}

	public static void addNewLook(int goodsId) {
		String sq0 = "use first_mysql_test";
		String sql1 = "update goods set look = look + 1 where goods_id = " + goodsId;
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			System.out.print(ps.toString());
			ps.executeUpdate();
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DBPool.close(con);
	}
}
