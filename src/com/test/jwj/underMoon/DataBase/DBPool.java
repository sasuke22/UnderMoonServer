package com.test.jwj.underMoon.DataBase;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

//import org.apache.commons.dbcp2.BasicDataSourceFactory;

import com.mysql.jdbc.Driver;
//import com.mysql.jdbc.PreparedStatement;
import com.test.jwj.underMoon.DataBase.PooledConnection;
//import javax.sql.DataSource;

/**
 * 使用数据库连接池加大响应速度
 *
 */
public class DBPool implements IMyPool{
//	private static DataSource ds;
//
//	private DBPool() {
//	}
//
//	static {
//		try {
//			InputStream in = DBPool.class.getClassLoader().getResourceAsStream(
//					"dbcpconfig.properties");
//			Properties pro = new Properties();
//			pro.load(in);
//			ds = BasicDataSourceFactory.createDataSource(pro);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static Connection getConnection() {
//		Connection con = null;
//		try {
//			con = ds.getConnection();
//		} catch (SQLException e) {
//			System.out.println("获取数据库连接失败....");
//			e.printStackTrace();
//		}
//		return con;
//	}
//
//	public static void close(Connection con) {
//		try {
//			con.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	private static String jdbcDriver = null;
    private static String jdbcUrl = null;
    private static String userName = null;
    private static String password = null;

    private static int initCount;

    private static int stepSize;

    private static int poolMaxSize;

    private static Vector<PooledConnection> pooledConnections = new Vector<PooledConnection>();

    public DBPool() {

//        init();
    }

    private void init() {

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("dbcpconfig.properties");

        //字节信息 key value 形式化
        Properties pro = new Properties();
        try {
            pro.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        jdbcDriver = pro.getProperty("driverClassName");
        jdbcUrl = pro.getProperty("url");
        userName = pro.getProperty("username");
        password = pro.getProperty("password");

        initCount = Integer.valueOf(pro.getProperty("initialSize"));
        stepSize = Integer.valueOf(pro.getProperty("stepSize"));
        poolMaxSize = Integer.valueOf(pro.getProperty("maxActive"));

        try {
            Driver mysqlDriver = (Driver) Class.forName(jdbcDriver).newInstance();

            DriverManager.registerDriver(mysqlDriver);

            createConnection(initCount);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    public Connection getConnection() {
//        if (pooledConnections.size() == 0) {
//            System.out.println("获取链接管道失败，原因是连接池中没有可用管道");
//            throw new RuntimeException("创建管道对象失败，原因是即将超过最大上限值");
//        }
//        //连接池中的管道是没有超时  没有其他线程占用
//        PooledConnection connection = getRealConnection();
//
//        while (connection == null) {
//            createConnection(stepSize);
//            connection = getRealConnection();
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return connection;
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://45.204.8.236:3306/first_mysql_test?&zeroDateTimeBehavior=convertToNull"
					,"root","sasuke12");//ds.getConnection();
		} catch (SQLException e) {
			System.out.println("获取数据库连接失败....");
			e.printStackTrace();
		}
		return con;
    }

    @Override
    public void createConnection(int count) {
        if (poolMaxSize > 0 && pooledConnections.size() + count > poolMaxSize) {
            System.out.println("创建管道对象失败，原因是即将超过最大上限值");
            throw new RuntimeException("创建管道对象失败，原因是即将超过最大上限值");
        }

        for (int i = 0; i < count; i++) {
            try {
                Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);

                PooledConnection pooledConnection = new PooledConnection(conn, false);
                pooledConnections.add(pooledConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private synchronized PooledConnection getRealConnection() {
        for (PooledConnection conn : pooledConnections) {
            if (!conn.isBusy()) {
                Connection connection = conn.getConnection();
                try {

                    //发送一个信息给数据库 2000毫秒内  收到返回信息 认为 这个管道没有超时
                    if (!connection.isValid(2000)) {
                        Connection validConn = DriverManager.getConnection(jdbcUrl, userName, password);
                        conn.setConnection(validConn);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn.setBusy(true);
                return conn;
            }
        }
        return null;
    }

}
