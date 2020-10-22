package com.qiqiim.webserver.user.dao;

import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;

import com.qiqiim.webserver.user.model.GoodsBean;
import org.apache.http.util.TextUtils;

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

	/**
	 * 上传商品
	 * @param goods 商品类
	 */
	public static int uploadGoods(GoodsBean goods) {
		if (TextUtils.isEmpty(goods.getGoodsName()) || TextUtils.isEmpty(goods.getGoodsPrice()))
			return -1;
		String sql0 = "use first_mysql_test";
		String sql1;
		sql1 = "insert into goods (goods_name, goods_price, goods_url, owner_id) values(?,?,?,?)";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		int result = 0;
		try {
			con.setAutoCommit(false);
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, URLEncoder.encode(goods.getGoodsName(),"utf-8"));
			ps.setString(2, goods.getGoodsPrice());
			ps.setString(3, goods.getGoodsUrl());
			ps.setInt(4, goods.getOwnerId());
			ps.execute();
			con.commit();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			result = -1;
		} finally {
			DBPool.close(con);
		}
		return result;
	}
}
