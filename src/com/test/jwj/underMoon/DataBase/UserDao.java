package com.test.jwj.underMoon.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.Result;


/**
 * 数据库操作
 * 
 */
public class UserDao {
	private static DBPool poolImpl = PoolManager.getInstance();
	private UserDao() {
	}

	/**
	 * 查询账号是否存在
	 * 
	 */
	public static boolean selectAccount(String account) {
		String sql0 = "use first_mysql_test";
		String sql1 = "select * from user where account=?";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		try {
//			con.setAutoCommit(false);
			PreparedStatement ps;
			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setString(1, account);
			ResultSet rs = ps.executeQuery();
			return rs.first() ? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		poolcon.close();
		return false;
	}

	/**
	 * 向数据库中添加账户
	 * 
	 */
	public static int insertInfo(User user) {
		String sql0 = "use first_mysql_test";
		String sql1 = "insert into user (account,name,photo,birthday,password,gender,city,age,height,marry,job,figure,xingzuo)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setString(1, user.getAccount());
			ps.setString(2, user.getUserName());
			ps.setBytes(3, user.getPhoto());
			System.out.println(user.getPhoto().length);
			ps.setDate(4, new java.sql.Date(user.getBirthday().getTime()));
			ps.setString(5, user.getPassword());
			ps.setInt(6, user.getGender());
			ps.setString(7, user.getLocation());
			ps.setInt(8, user.getAge());
			ps.setInt(9, user.getHeight());
			ps.setInt(10, user.getMarry());
			ps.setString(11, user.getJob());
			ps.setString(12, user.getFigure());
			ps.setString(13, user.getXingzuo());
			ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				System.out.println("插入数据库异常，正在进行回滚..");
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return getLastID(poolcon,con);
	}

	/**
	 * 得到最后一次插入的值
	 */
	public static int getLastID(PooledConnection poolcon,Connection con) {
		String sql0 = "use first_mysql_test";
		String sql1 = "select MAX(id) as ID from user";// 注意:使用MAX(ID) 必须加上 as
														// id 翻译
		PreparedStatement ps;
		ResultSet rs;
		int id = -1;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			rs = ps.executeQuery();
			if (rs.first())
				id = rs.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		poolcon.close();
		return id;

	}

	/**
	 * 进行登录的验证
	 */
	public static boolean login(User user) {
		boolean isExisted = false;
		String sql0 = "use first_mysql_test";
		String sql1 = "select * from user where account=? and password=?";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setString(1, user.getAccount());
			ps.setString(2, user.getPassword());
			rs = ps.executeQuery();
			if (rs.first()) {
				isExisted = true;
				// 为用户添加自己的id
				user.setId(rs.getInt("id"));
				user.setAccount(rs.getString("account"));
				user.setBirthday(rs.getDate("birthday"));
				user.setGender(rs.getInt("gender"));
				user.setPassword(rs.getString("password"));
				user.setUserName(rs.getString("name"));
				user.setPhoto(rs.getBytes("photo"));
				user.setLocation(rs.getString("city"));
				user.setAge(rs.getInt("age"));
				user.setHeight(rs.getInt("height"));
				user.setMarry(rs.getInt("marry"));
				user.setJob(rs.getString("job"));
				user.setFigure(rs.getString("figure"));
				user.setXingzuo(rs.getString("xingzuo"));
				user.setLoveType(rs.getString("lovetype"));
				user.setScore(rs.getInt("score"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		poolcon.close();
		return isExisted;
	}

	/**
	 * 更新在线状态
	 */
	public static void updateIsOnline(int id, int isOnline) {
		String sql0 = "use first_mysql_test";
		String sql1 = "update user set isOnline=? where id=?";
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
			ps.setInt(1, isOnline);
			ps.setInt(2, id);
			ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				System.out.println("数据库正在回滚....");
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		poolcon.close();
	}

	public static ArrayList<User> selectFriendByAccountOrID(Object condition) {
		ArrayList<User> list = new ArrayList<User>();
		String sql0 = "use first_mysql_test";
		String sql1 = "";
		int conFlag = 0;// 默认是0 表示使用id查找 1为使用id
		if (condition instanceof String) {
			sql1 = "select * from user where id=?";
			conFlag = 1;
		} else if (condition instanceof Integer)
			sql1 = "select * from user where id=?";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			if (conFlag == 1)
				ps.setString(1, (String) condition);
			else if (conFlag == 0)
				ps.setInt(1, (Integer) condition);
			rs = ps.executeQuery();
			while (rs.next()) {
				User friend = new User();
				friend.setId(rs.getInt("id"));
				friend.setAccount(rs.getString("account"));
				friend.setBirthday(rs.getDate("birthday"));
				friend.setGender(rs.getInt("gender"));
				friend.setUserName(rs.getString("name"));
				if (rs.getInt("isOnline") == 1)
					friend.setIsOnline(true);
				else
					friend.setIsOnline(false);
				friend.setPhoto(rs.getBytes("photo"));
				friend.setLocation(rs.getString("city"));
				list.add(friend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		poolcon.close();
		return list;
	}

	public static ArrayList<User> selectFriendByMix(String[] mix) {
		ArrayList<User> list = new ArrayList<User>();
		String sql0 = "use first_mysql_test";
		String sql1 = "select * "
				+ "from user "
				+ "where ((YEAR(CURDATE())-YEAR(birthday))-(RIGHT(CURDATE(),5)<RIGHT(birthday,5))) "
				+ "between ? and ? ";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			if (mix[3].equals("3"))
				sql1 += "and gender=1 or gender=0";
			else if (mix[3].equals("1"))
				sql1 += "and gender=1";
			else if (mix[3].equals("0"))
				sql1 += "and gender=0";
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, Integer.parseInt(mix[1]));
			ps.setInt(2, Integer.parseInt(mix[2]));
			rs = ps.executeQuery();
			while (rs.next()) {
				User friend = new User();
				friend.setId(rs.getInt("id"));
				friend.setAccount(rs.getString("account"));
				friend.setBirthday(rs.getDate("birthday"));
				friend.setGender(rs.getInt("gender"));
				friend.setUserName(rs.getString("name"));
				if (rs.getInt("isOnline") == 1)
					friend.setIsOnline(true);
				else
					friend.setIsOnline(false);
				friend.setPhoto(rs.getBytes("photo"));
				friend.setLocation(rs.getString("location"));
				list.add(friend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		poolcon.close();
		return list;
	}

	public static int saveUserInfo(TranObject tran){
		User user = (User)tran.getObject();
		String sql0 = "use first_mysql_test";
		String sql1 = "update user SET age = ? ,height = ? ,figure = ? ,job = ? ,lovetype = ? ,marry = ? "
				+ ",xingzuo = ? ,city = ? where id = ? ";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try{
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, user.getAge());
			ps.setInt(2, user.getHeight());
			ps.setString(3, user.getFigure());
			ps.setString(4, user.getJob());
			ps.setString(5, user.getLoveType());
			ps.setInt(6, user.getMarry());
			ps.setString(7, user.getXingzuo());
			ps.setString(8, user.getLocation());
			ps.setInt(9, user.getId());
			ps.executeUpdate();
			con.commit();
			poolcon.close();
			return 1;
		}catch (SQLException e) {
			e.printStackTrace();
			poolcon.close();
			return 0;
		}
	}
	
	public static Result updateRegist(TranObject tran){
		ArrayList<String> registArray = queryRegist(tran);
		if (registArray.contains(String.valueOf((Integer)tran.getObject()))) {
			return Result.ENLIST_EXIST;
		}
		String sql0 = "use first_mysql_test";
		String sql1 = "update user set registId= case when isnull(registId) or registId='' then ? else concat(registId,'|',?) end where id =?";
		int userId = tran.getSendId();
		int meetingId = (Integer)tran.getObject();
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
			ps.setInt(2, meetingId);
			ps.setInt(3, userId);
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
	
	public static ArrayList<String> queryRegist(TranObject tran){
		String sql0 = "use first_mysql_test";
		String sql1 = "select registId from user where id = ?";
		ArrayList<String> registArray = new ArrayList<String>();
		int userId = tran.getSendId();
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
			ps.setInt(1, userId);
			System.out.println(ps.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				String id = rs.getString("registId");
				String[] idArray = id.split("\\|");
				for (String string : idArray) {
					if (!string.equalsIgnoreCase("|")) {
						if (string.equals(String.valueOf(userId))) {
							continue;
						}else
							registArray.add(string);
					}
				}
			}
			poolcon.close();
			return registArray;
		}catch (Exception e){
			poolcon.close();
			return null;
		}
	}
	
	public static User getUserInfo(TranObject tran){
		int userId = (Integer) tran.getObject();
		String sql0 = "use first_mysql_test";
		String sql1 = "select * from user where id=?";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		User enlister = new User();
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				enlister.setId(rs.getInt("id"));
				enlister.setAccount(rs.getString("account"));
				enlister.setBirthday(rs.getDate("birthday"));
				enlister.setGender(rs.getInt("gender"));
				enlister.setUserName(rs.getString("name"));
				enlister.setPhoto(rs.getBytes("photo"));
				enlister.setLocation(rs.getString("city"));
				enlister.setAge(rs.getInt("age"));
				enlister.setJob(rs.getString("job"));
				enlister.setPhotoAddress(rs.getString("photos"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		poolcon.close();
		return enlister;
	}

	public static String getUserPhotosAddress(int userId) {
		String sql0 = "use first_mysql_test";
		String sql1 = "select photos from user where id = ?";
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
			ps.setInt(1, userId);
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
	
	public static Result updatePhotos(int userId,int lastPhoto){
		String sql0 = "use first_mysql_test";
		String sql1 = "update user set photos= case when isnull(photos) or photos='' then ? else concat(photos,'|',?) end where id =?";
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
			ps.setInt(3, userId);
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
	
	public static int updateScore(int id,int value){
		String sql0 = "use first_mysql_test";
		String sql1 = "update user SET score = " + value + " where id = ?";
		PooledConnection poolcon = poolImpl.getConnection();
		Connection con = poolcon.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try{
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, id);
			System.out.println(ps);
			ps.executeUpdate();
			con.commit();
			poolcon.close();
			return 1;
		}catch (SQLException e) {
			e.printStackTrace();
			poolcon.close();
			return 0;
		}
	}
}
