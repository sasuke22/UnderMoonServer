package com.qiqiim.webserver.user.dao;

import com.qiqiim.webserver.user.model.OrderBean;
import org.apache.http.util.TextUtils;
import java.sql.*;

public class OrderDao {
    /**
     * 上传商品
     */
    public static int createOrder(OrderBean order) {
        if (TextUtils.isEmpty(order.goodsId) || TextUtils.isEmpty(order.buyId))
            return -1;
        String sql0 = "use first_mysql_test";
        String sql1;
        sql1 = "insert into order (buy_id, goods_id, date, state) values(?,?, CURDATE(),?)";
        Connection con = DBPool.getConnection();
        PreparedStatement ps;
        int result = 0;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql0);
            ps.execute();
            ps = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, order.buyId);
            ps.setString(2, order.goodsId);
            ps.setInt(3, 0);
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

    public static void updateOrderStatus(int status, int order_id) {
        String sq0 = "use first_mysql_test";
        String sql1 = "update order set state = "+ status +" where order_id = " + order_id;
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
