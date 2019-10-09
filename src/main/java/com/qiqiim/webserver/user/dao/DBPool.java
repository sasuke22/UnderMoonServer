package com.qiqiim.webserver.user.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSourceFactory;


import javax.sql.DataSource;

/**
 * 使用数据库连接池加大响应速度
 *
 */
public class DBPool {
	private static DataSource ds;

	public DBPool() {
	}

	static {
		try {
			InputStream in = DBPool.class.getClassLoader().getResourceAsStream(
					"db.properties");
			Properties pro = new Properties();
			pro.load(in);
			ds = BasicDataSourceFactory.createDataSource(pro);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/first_mysql_test?&zeroDateTimeBehavior=convertToNull"
					,"root","sasuke12");//ds.getConnection();
		} catch (SQLException e) {
			System.out.println("获取数据库连接失败....");
			e.printStackTrace();
		}
		return con;
	}

	public static void close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
