package com.qiqiim.webserver.user.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.qiqiim.constant.User;


/**
 * 数据库操作
 * 
 */
public class UserDao {

	private UserDao() {
	}

	/**
	 * 查询账号是否存在
	 * 
	 */
	public static boolean selectAccount(String account,boolean isLogin) {
		String sql0 = "use first_mysql_test";
		String sql1 = "select * from user where account=?";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
			PreparedStatement ps;
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setString(1, account);
			ResultSet rs = ps.executeQuery();
			if(rs.first()){
				if(!isLogin)//注册，查重
					return true;
				else//登陆，查锁
					return rs.getInt("isLock") == 1;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBPool.close(con);
		return false;
	}

	/**
	 * 向数据库中添加账户
	 * 
	 */
	public static int insertInfo(User user) {
		String sql0 = "use first_mysql_test";
		String sql1 = "insert into user (account,name,password,gender,city,age,height,marry,job,xingzuo)"
				+ " values(?,?,?,?,?,?,?,?,?,?)";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getAccount());
			ps.setString(2, user.getUserName());
			ps.setString(3, user.getPassword());
			ps.setInt(4, user.getGender());
			ps.setString(5, user.getLocation());
			ps.setInt(6, user.getAge());
			ps.setInt(7, user.getHeight());
			ps.setString(8, user.getMarry());
			ps.setString(9, user.getJob());
			ps.setString(10, user.getXingzuo());
			ps.execute();
			con.commit();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			try {
				System.out.println("插入数据库异常，正在进行回滚..");
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			DBPool.close(con);
			return -1;
		}
		DBPool.close(con);
		return 0;
	}

	/**
	 * 得到最后一次插入的值
	 */
	public static int getLastID(Connection con) {
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
			DBPool.close(con);
		}
		DBPool.close(con);
		return id;

	}

	/**
	 * 进行登录的验证
	 */
	public static boolean login(User user) {
		boolean isExisted = false;
		String sql0 = "use first_mysql_test";
		String sql1 = "select * from user where account=? and password=?";
//		PooledConnection poolcon = poolImpl.getConnection();
//		Connection con = poolcon.getConnection();
		Connection con = DBPool.getConnection();
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
				user.setGender(rs.getInt("gender"));
				user.setPassword(rs.getString("password"));
				user.setUserName(rs.getString("name"));
				user.setLocation(rs.getString("city"));
				user.setAge(rs.getInt("age"));
				user.setHeight(rs.getInt("height"));
				user.setMarry(rs.getString("marry"));
				user.setJob(rs.getString("job"));
				user.setUserBriefIntro(rs.getString("userintro"));
				user.setXingzuo(rs.getString("xingzuo"));
				user.setScore(rs.getInt("score"));
				user.setCommentDate(rs.getDate("commentDate"));
				user.setShow(rs.getInt("showname") > 0);
				if(rs.getTimestamp("vip") != null)
				user.setVipDate(new Date(rs.getTimestamp("vip").getTime()));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		poolcon.close();
		return isExisted;
	}

	/**
	 * 更新在线状态
	 */
	public static void updateIsOnline(int id, int isOnline) {
		String sql0 = "use first_mysql_test";
		String sql1 = "update user set isOnline=? where id=?";
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
			ps = con.prepareStatement(sql1);
			ps.setInt(1, isOnline);
			ps.setInt(2, id);
			ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				System.out.println("数据库正在回滚....");
				con.rollback();
				DBPool.close(con);
			} catch (SQLException e1) {
				e1.printStackTrace();
				DBPool.close(con);
			}
			e.printStackTrace();
			DBPool.close(con);
		}
		DBPool.close(con);
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
		Connection con = DBPool.getConnection();
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
				friend.setGender(rs.getInt("gender"));
				friend.setAge(rs.getInt("age"));
				friend.setUserName(rs.getString("name"));
				if (rs.getInt("isOnline") == 1)
					friend.setIsOnline(true);
				else
					friend.setIsOnline(false);
				friend.setLocation(rs.getString("city"));
				list.add(friend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBPool.close(con);
		}
		DBPool.close(con);
		return list;
	}

	public static ArrayList<User> selectFriendByMix(String[] mix) {
		ArrayList<User> list = new ArrayList<User>();
		String sql0 = "use first_mysql_test";
		String sql1 = "select * "
				+ "from user "
				+ "where ((YEAR(CURDATE())-YEAR(birthday))-(RIGHT(CURDATE(),5)<RIGHT(birthday,5))) "
				+ "between ? and ? ";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			if (mix[2].equals("3"))
				sql1 += "and gender=1 or gender=0";
			else if (mix[2].equals("1"))
				sql1 += "and gender=1";
			else if (mix[2].equals("0"))
				sql1 += "and gender=0";
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, Integer.parseInt(mix[0]));
			ps.setInt(2, Integer.parseInt(mix[1]));
			rs = ps.executeQuery();
			while (rs.next()) {
				User friend = new User();
				friend.setId(rs.getInt("id"));
				friend.setAccount(rs.getString("account"));
				friend.setGender(rs.getInt("gender"));
				friend.setUserName(rs.getString("name"));
				if (rs.getInt("isOnline") == 1)
					friend.setIsOnline(true);
				else
					friend.setIsOnline(false);
				friend.setLocation(rs.getString("city"));
				list.add(friend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBPool.close(con);
		}
		DBPool.close(con);
		return list;
	}
	
	public static ArrayList<User> selectFriendByFilter(int userId,int gender,int way,String text,int min,int max) {
		ArrayList<User> list = new ArrayList<User>();
		String sql0 = "use first_mysql_test";
		String sql1 = "select * from user where ";
		String textFilter = "";
		String genderFilter = "";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		switch (gender) {
		case 0:
			genderFilter = "and gender = 0";
			break;
		case 1:
			genderFilter = "and gender = 1";
			break;
		default:
			genderFilter = "and gender = 0 or gender = 1";
			break;
		}
		if (text.length() > 0) {
			if (way == 0) 
				textFilter = "id = " + text + " and ";
			else
				textFilter = "name = '" + text + "' and ";
		}
		sql1 += textFilter
				+ "((YEAR(CURDATE())-YEAR(birthday))-(RIGHT(CURDATE(),5)<RIGHT(birthday,5))) between ? and ? "
				+ genderFilter;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, min);
			ps.setInt(2, max);
			rs = ps.executeQuery();
			while (rs.next()) {
				if(rs.getInt("id") == userId){
					continue;
				}
				User friend = new User();
				friend.setId(rs.getInt("id"));
				friend.setAccount(rs.getString("account"));
				friend.setAge(rs.getInt("age"));
				friend.setGender(rs.getInt("gender"));
				friend.setUserName(rs.getString("name"));
				if (rs.getInt("isOnline") == 1)
					friend.setIsOnline(true);
				else
					friend.setIsOnline(false);
				friend.setLocation(rs.getString("city"));
				list.add(friend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBPool.close(con);
		}
		DBPool.close(con);
		return list;
	}

	public static int saveUserInfo(User user){
		System.out.println(user.toString());
		String sql0 = "use first_mysql_test";
		String sql1 = "update user SET age = ? ,height = ? ,userintro = ? ,job = ? ,marry = ? "
				+ ",xingzuo = ? ,city = ? ,name = ? where id = ? ";
		Connection con = DBPool.getConnection();
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
			ps.setString(3, user.getUserBriefIntro());
			ps.setString(4, user.getJob());
			ps.setString(5, user.getMarry());
			ps.setString(6, user.getXingzuo());
			ps.setString(7, user.getLocation());
			ps.setString(8, user.getUserName());
			ps.setInt(9, user.getId());
			ps.executeUpdate();
			con.commit();
			DBPool.close(con);
			return 1;
		}catch (SQLException e) {
			e.printStackTrace();
			DBPool.close(con);
			return 0;
		}finally{
			DBPool.close(con);
		}
	}
	
	public static int updateRegist(int userId,int meetingId){
		ArrayList<String> registArray = queryRegist(userId);
		if (registArray == null || !registArray.contains(String.valueOf(meetingId))) {
			String sql0 = "use first_mysql_test";
			String sql1 = "update user set commentDate = CURDATE(),registId= case when isnull(registId) or registId='' then ? else concat(registId,'|',?) end where id =?";
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
				ps = con.prepareStatement(sql1);
				ps.setInt(1, meetingId);
				ps.setInt(2, meetingId);
				ps.setInt(3, userId);
				System.out.println(ps.toString());
				ps.execute();
				con.commit();
				DBPool.close(con);
				return 1;
			}catch (Exception e){
				e.printStackTrace();
				DBPool.close(con);
				return -1;
			}
		}else 
			return 0;
	}
	
	public static ArrayList<String> queryRegist(int userId){
		String sql0 = "use first_mysql_test";
		String sql1 = "select registId from user where id = ?";
		ArrayList<String> registArray = new ArrayList<String>();
		Connection con = DBPool.getConnection();
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
				if(id != null){
					String[] idArray = id.split("\\|");
					for (String string : idArray) {
						System.out.println("meetingId:"+string);
						if (!string.equalsIgnoreCase("\\|")) {
							if (string.equals(String.valueOf(userId))) {
								continue;
							}else
								registArray.add(string);
						}
					}
				} else 
					return null;
			}
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}finally{
			DBPool.close(con);
		}
		return registArray;
	}
	
	public static User getUserInfo(int userId){
		String sql0 = "use first_mysql_test";
		String sql1 = "select * from user where id=?";
		Connection con = DBPool.getConnection();
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
				enlister.setGender(rs.getInt("gender"));
				enlister.setUserName(rs.getString("name"));
				enlister.setLocation(rs.getString("city"));
				enlister.setAge(rs.getInt("age"));
				enlister.setHeight(rs.getInt("height"));
				enlister.setXingzuo(rs.getString("xingzuo"));
				enlister.setMarry(rs.getString("marry"));
				enlister.setJob(rs.getString("job"));
				enlister.setPhotoAddress(rs.getString("photos"));
				enlister.setUserBriefIntro(rs.getString("userintro"));
				enlister.setCommentDate(rs.getDate("commentDate"));
				enlister.setVipDate(new Date(rs.getTimestamp("vip").getTime()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBPool.close(con);
		}
		DBPool.close(con);
		return enlister;
	}
	
	public static String getUserPhotosAddress(int userId) {
		String sql0 = "use first_mysql_test";
		String sql1 = "select photos from user where id = ?";
		Connection con = DBPool.getConnection();
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
			DBPool.close(con);
			return id;
		}catch (Exception e){
			System.out.println("select regist " + e.getMessage().toString());
			DBPool.close(con);
			return null;
		}
	}
	
	public static void updatePhotos(int userId,String lastPhoto){
		String sql0 = "use first_mysql_test";
		String sql1 = "update user set photos= case when isnull(photos) or photos='' then ? else concat(photos,'|',?) end where id =?";
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
			ps = con.prepareStatement(sql1);
			ps.setString(1, lastPhoto);
			ps.setString(2, lastPhoto);
			ps.setInt(3, userId);
			System.out.println(ps.toString());
			ps.execute();
			con.commit();
			DBPool.close(con);
		}catch (Exception e){
			DBPool.close(con);
		}
	}
	
	public static int updateScore(int id,int value){
		String sql0 = "use first_mysql_test";
		String sql1 = "update user SET score = score + " + value + " where id = ?";
		Connection con = DBPool.getConnection();
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
			DBPool.close(con);
			return 1;
		}catch (SQLException e) {
			e.printStackTrace();
			DBPool.close(con);
			return 0;
		}
	}
	
	public static int updatePassword(int id,String password){
		String sql0 = "use first_mysql_test";
		String sql1 = "update user SET password = " + password + " where id = ?";
		Connection con = DBPool.getConnection();
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
			DBPool.close(con);
			return 1;
		}catch (SQLException e) {
			e.printStackTrace();
			DBPool.close(con);
			return 0;
		}
	}
	
	public static int deletePic(int userId,String indexString){
		String sql0 = "use first_mysql_test";
		String sql1 = "update user SET photos = ? where id = ?";
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
			ps = con.prepareStatement(sql1);
			ps.setString(1, indexString);
			ps.setInt(2, userId);
			System.out.println(ps.toString());
			ps.execute();
			con.commit();
			DBPool.close(con);
			return 1;
		}catch (Exception e){
			DBPool.close(con);
			return 0;
		}
	}

	public static ArrayList<User> selectAllUsersByOldCount(int oldCount,int gender) {
		ArrayList<User> userList = new ArrayList<User>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from user where gender = ? order by id desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, gender);
			ps.setInt(2, oldCount);
			ps.setInt(3, oldCount + 20);
			rs = ps.executeQuery();
			while (rs.next()) {
				User enlister = new User();
				enlister.setId(rs.getInt("id"));
				enlister.setAccount(rs.getString("account"));
				enlister.setGender(rs.getInt("gender"));
				enlister.setUserName(rs.getString("name"));
				enlister.setLocation(rs.getString("city"));
				enlister.setAge(rs.getInt("age"));
				enlister.setHeight(rs.getInt("height"));
				enlister.setXingzuo(rs.getString("xingzuo"));
				enlister.setMarry(rs.getString("marry"));
				enlister.setJob(rs.getString("job"));
				enlister.setPhotoAddress(rs.getString("photos"));
				enlister.setUserBriefIntro(rs.getString("userintro"));
				enlister.setScore(rs.getInt("score"));
				enlister.setLock(rs.getInt("isLock"));
				enlister.setPassword(rs.getString("password"));
				userList.add(enlister);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
			DBPool.close(con);
		}
		DBPool.close(con);
		return userList;
	}

	public static int disableUser(int id, int lock) {
		String sql1;
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			PreparedStatement ps;
			sql1 = "update user SET isLock = ? where id = ?";
			ps = con.prepareStatement(sql1);
			ps.setInt(1, lock);
			ps.setInt(2, id);
			ps.executeUpdate();
			con.commit();
			DBPool.close(con);
			return 1;
		}catch (SQLException e) {
			e.printStackTrace();
			DBPool.close(con);
			return -1;
		}
	}

	public static HashMap<String, Integer> getUserCount() {
		String sql1;
		Connection con = DBPool.getConnection();
		HashMap<String,Integer> map;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			PreparedStatement ps;
			ResultSet rs;
			sql1 = "select sum(case when gender = 1 then 1 else 0 end)as man,sum(case when gender = 0 then 1 else 0 end)as woman from user;";
			ps = con.prepareStatement(sql1);
			rs = ps.executeQuery();
			int manCount = 0,womanCount = 0;
			if(rs.next()){
				manCount = rs.getInt("man");
				womanCount = rs.getInt("woman");
			}
			map = new HashMap<>();
			map.put("man", manCount);
			map.put("woman", womanCount);
		}catch (SQLException e) {
			e.printStackTrace();
			map = null;
		}finally{
			DBPool.close(con);
		}
		return map;
	}
	
	public static int makeUserVIP(int id) {
		String sql1;
		Connection con = DBPool.getConnection();
		int res;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			PreparedStatement ps;
			sql1 = "update user SET vip = timestampadd(year,1,CURRENT_TIMESTAMP), score = score + 225 where id = ?";
			ps = con.prepareStatement(sql1);
			ps.setInt(1, id);
			ps.executeUpdate();
			con.commit();
			res = 1;
		}catch (SQLException e) {
			e.printStackTrace();
			res = -1;
		}finally{
			DBPool.close(con);
		}
		return res;
	}
	
	public static int getScore(int id){
		String sql1;
		ResultSet rs;
		int score = 0;
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			PreparedStatement ps;
			sql1 = "select score from user where id = ?";
			ps = con.prepareStatement(sql1);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.first())
				score = rs.getInt("score");
		}catch (SQLException e) {
			e.printStackTrace();
			score = -1;
		}finally{
			DBPool.close(con);
		}
		return score;
	}
	
	public static void updateCommentDate(int id){
		String sql1;
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			PreparedStatement ps;
			sql1 = "update user SET commentDate = CURDATE() where id = ?";
			ps = con.prepareStatement(sql1);
			ps.setInt(1, id);
			ps.executeUpdate();
			con.commit();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
	}

	public static int changeShow(int userId, boolean show) {
		String sql1;
		Connection con = DBPool.getConnection();
		int res;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			PreparedStatement ps;
			sql1 = "update user SET showname = ? where id = ?";
			ps = con.prepareStatement(sql1);
			ps.setInt(1, show ? 1 : 0);
			ps.setInt(2, userId);
			ps.executeUpdate();
			con.commit();
			res = 1;
		}catch (SQLException e) {
			e.printStackTrace();
			res = -1;
		}finally{
			DBPool.close(con);
		}
		return res;
	}
}
