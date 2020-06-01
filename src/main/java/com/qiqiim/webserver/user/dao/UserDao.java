package com.qiqiim.webserver.user.dao;

import com.qiqiim.constant.User;
import com.qiqiim.webserver.util.AESUtil;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


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
		boolean res = false;
		try {
			con.setAutoCommit(false);
			PreparedStatement ps;
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setString(1, account);
			ResultSet rs = ps.executeQuery();
			if(rs.first()){
				if(!isLogin){//注册，查重
					res = true;
				}else{//登陆，查锁
					res = rs.getInt("isLock") == 1;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			res = false;
		}finally{
			DBPool.close(con);
		}
		return res;
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
			return -1;
		}finally{
			DBPool.close(con);
		}
		return 0;
	}

	/**
	 * 进行登录的验证
	 */
	public static boolean login(String account, String password) {
		boolean isExisted = false;
		String sql0 = "use first_mysql_test";
		String sql1 = "select * from user where account=? and password=? limit 1";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setString(1, account);
			ps.setString(2, password);
			rs = ps.executeQuery();
			return rs.first();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
		return isExisted;
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
				user.setUserBriefIntro(URLDecoder.decode(rs.getString("userintro") == null ? "" : rs.getString("userintro"),"utf-8"));
				user.setXingzuo(rs.getString("xingzuo"));
				user.setScore(rs.getInt("score"));
				user.setCommentDate(rs.getDate("commentDate"));
				user.setShow(rs.getInt("showname") > 0);
				if(rs.getTimestamp("vip") != null)
				user.setVipDate(new Date(rs.getTimestamp("vip").getTime()));
				user.setBigVip(rs.getDate("bigVip"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
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
//				+ "where ((YEAR(CURDATE())-YEAR(birthday))-(RIGHT(CURDATE(),5)<RIGHT(birthday,5))) "
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
			ps.setString(3, URLEncoder.encode(user.getUserBriefIntro(),"utf-8"));
			ps.setString(4, user.getJob());
			ps.setString(5, user.getMarry());
			ps.setString(6, user.getXingzuo());
			ps.setString(7, user.getLocation());
			ps.setString(8, user.getUserName());
			ps.setInt(9, user.getId());
			ps.executeUpdate();
			con.commit();
			return 1;
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}finally{
			DBPool.close(con);
		}
	}
	
	public static int updateRegist(int userId,int meetingId){
		ArrayList<String> registArray = queryRegist(userId);
		if (registArray == null || !registArray.contains(String.valueOf(meetingId))) {
			String sql0 = "use first_mysql_test";
			String sql1 = "update user set registId= case when isnull(registId) or registId='' then ? else concat(registId,'|',?) end where id =?";
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
				return 1;
			}catch (Exception e){
				e.printStackTrace();
				return -1;
			}finally{
				DBPool.close(con);
			}
		}else 
			return 0;
	}
	
	public static int cancelEnlist(int userId,int meetingId){
		ArrayList<String> registArray = queryRegist(userId);
		if (registArray != null) {
			StringBuilder builder = new StringBuilder();
			for(String id : registArray){
				if(Integer.parseInt(id) != meetingId){
					builder.append(id).append("|");
				}
			}
			String sql0 = "use first_mysql_test";
			String sql1 = "update user set registId= ? where id =?";
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
				ps.setString(1, builder.toString());
				ps.setInt(2, userId);
				ps.execute();
				con.commit();
				return 1;
			}catch (Exception e){
				e.printStackTrace();
				return -1;
			}finally{
				DBPool.close(con);
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
						//如果不是分割线就加到报名列表
						if (!string.equalsIgnoreCase("\\|"))
							registArray.add(string);
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
				enlister.setAccount("");
				enlister.setPassword("");
				enlister.setGender(rs.getInt("gender"));
				enlister.setUserName(rs.getString("name"));
				enlister.setLocation(rs.getString("city"));
				enlister.setAge(rs.getInt("age"));
				enlister.setHeight(rs.getInt("height"));
				enlister.setXingzuo(rs.getString("xingzuo"));
				enlister.setMarry(rs.getString("marry"));
				enlister.setJob(rs.getString("job"));
				enlister.setPhotoAddress(rs.getString("photos"));
				enlister.setUserBriefIntro(URLDecoder.decode(rs.getString("userintro") == null ? "" : rs.getString("userintro"),"utf-8"));
				enlister.setCommentDate(rs.getDate("commentDate"));
				enlister.setVipDate(new Date(rs.getTimestamp("vip").getTime()));
				enlister.setBigVip(rs.getDate("bigVip"));
				enlister.setLock(rs.getInt("islock"));
				enlister.setScore(rs.getInt("score"));
				enlister.setZhencheng(rs.getInt("zhencheng"));
				enlister.setJingji(rs.getInt("jingji"));
				enlister.setChuangpin(rs.getInt("chuangpin"));
				enlister.setNeihan(rs.getInt("neihan"));
				enlister.setShencai(rs.getInt("shencai"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
		return enlister;
	}

	public static User getUserInfoServer(int userId){
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
				enlister.setAccount(AESUtil.encrypt(rs.getString("account")));
				enlister.setPassword(rs.getString("password"));
				enlister.setGender(rs.getInt("gender"));
				enlister.setUserName(rs.getString("name"));
				enlister.setLocation(rs.getString("city"));
				enlister.setAge(rs.getInt("age"));
				enlister.setHeight(rs.getInt("height"));
				enlister.setXingzuo(rs.getString("xingzuo"));
				enlister.setMarry(rs.getString("marry"));
				enlister.setJob(rs.getString("job"));
				enlister.setPhotoAddress(rs.getString("photos"));
				enlister.setUserBriefIntro(URLDecoder.decode(rs.getString("userintro") == null ? "" : rs.getString("userintro"),"utf-8"));
				enlister.setCommentDate(rs.getDate("commentDate"));
				enlister.setVipDate(new Date(rs.getTimestamp("vip").getTime()));
				enlister.setBigVip(rs.getDate("bigVip"));
				enlister.setLock(rs.getInt("islock"));
				enlister.setScore(rs.getInt("score"));
				enlister.setZhencheng(rs.getInt("zhencheng"));
				enlister.setJingji(rs.getInt("jingji"));
				enlister.setChuangpin(rs.getInt("chuangpin"));
				enlister.setNeihan(rs.getInt("neihan"));
				enlister.setShencai(rs.getInt("shencai"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
		return enlister;
	}

	public static User getUserInfoByAccount(String account){
		String sql0 = "use first_mysql_test";
		String sql1 = "select * from user where account=?";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		User enlister = new User();
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setString(1, account);
			rs = ps.executeQuery();
			while (rs.next()) {
				enlister.setId(rs.getInt("id"));
				enlister.setAccount(AESUtil.encrypt(rs.getString("account")));
				enlister.setPassword(rs.getString("password"));
				enlister.setGender(rs.getInt("gender"));
				enlister.setUserName(rs.getString("name"));
				enlister.setLocation(rs.getString("city"));
				enlister.setAge(rs.getInt("age"));
				enlister.setHeight(rs.getInt("height"));
				enlister.setXingzuo(rs.getString("xingzuo"));
				enlister.setMarry(rs.getString("marry"));
				enlister.setJob(rs.getString("job"));
				enlister.setPhotoAddress(rs.getString("photos"));
				enlister.setUserBriefIntro(URLDecoder.decode(rs.getString("userintro") == null ? "" : rs.getString("userintro"),"utf-8"));
				enlister.setCommentDate(rs.getDate("commentDate"));
				enlister.setVipDate(new Date(rs.getTimestamp("vip").getTime()));
				enlister.setBigVip(rs.getDate("bigVip"));
				enlister.setLock(rs.getInt("islock"));
				enlister.setScore(rs.getInt("score"));
				enlister.setZhencheng(rs.getInt("zhencheng"));
				enlister.setJingji(rs.getInt("jingji"));
				enlister.setChuangpin(rs.getInt("chuangpin"));
				enlister.setNeihan(rs.getInt("neihan"));
				enlister.setShencai(rs.getInt("shencai"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
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
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("select regist " + e.getMessage().toString());
		}finally{
			DBPool.close(con);
		}
		return id;
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
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
	}
	
	public static int updateScore(int id,int value){
		String sql0 = "use first_mysql_test";
		String sql1;
		if(value == -1){//评论减分
			sql1 = "update user SET commentDate = CURDATE(),score = score + " + value + " where id = ?";
		} else
			sql1 = "update user SET score = score + " + value + " where id = ?";
		Connection con = DBPool.getConnection();
		int res = 0;
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
			res = 1;
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
		return res;
	}
	
	public static int updatePassword(int id,String password){
		String sql0 = "use first_mysql_test";
		String sql1 = "update user SET password = ? where id = ?";
		Connection con = DBPool.getConnection();
		int res = 0;
		PreparedStatement ps;
		try{
			con.setAutoCommit(false);
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setString(1,password);
			ps.setInt(2, id);
			System.out.println(ps);
			ps.executeUpdate();
			con.commit();
			res = 1;
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
		return res;
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
			return 1;
		}catch (Exception e){
			return 0;
		}finally{
			DBPool.close(con);
		}
	}

	public static ArrayList<User> selectAllUsersByOldCount(int oldCount,int gender) {
		ArrayList<User> userList = new ArrayList<User>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from user where gender = ? order by bigVip desc,vip desc,id desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, gender);
			ps.setInt(2, oldCount);
			ps.setInt(3, 20);
			rs = ps.executeQuery();
			while (rs.next()) {
				User enlister = new User();
				enlister.setId(rs.getInt("id"));
				enlister.setAccount(AESUtil.encrypt(rs.getString("account")));
				enlister.setGender(rs.getInt("gender"));
				enlister.setUserName(rs.getString("name"));
				enlister.setLocation(rs.getString("city"));
				enlister.setAge(rs.getInt("age"));
				enlister.setHeight(rs.getInt("height"));
				enlister.setXingzuo(rs.getString("xingzuo"));
				enlister.setMarry(rs.getString("marry"));
				enlister.setJob(rs.getString("job"));
				enlister.setPhotoAddress(rs.getString("photos"));
				enlister.setUserBriefIntro(URLDecoder.decode(rs.getString("userintro") == null ? "" : rs.getString("userintro"),"utf-8"));
				enlister.setScore(rs.getInt("score"));
				enlister.setLock(rs.getInt("isLock"));
				enlister.setPassword(rs.getString("password"));
				enlister.setVipDate(new Date(rs.getTimestamp("vip").getTime()));
				enlister.setBigVip(rs.getDate("bigVip"));
				userList.add(enlister);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			DBPool.close(con);
		}
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
			return 1;
		}catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}finally{
			DBPool.close(con);
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
			sql1 = "update user SET vip = timestampadd(year,1,CURRENT_TIMESTAMP), score = score + 300 where id = ?";
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
	
	public static int makeUserBigVIP(int id) {
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
			sql1 = "update user set bigVip = '2099/01/01',score = score + 600 where id  = ?";
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

	public static String queryPassword(String phone) {
		String sql1;
		ResultSet rs;
		String password = "";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
			PreparedStatement ps;
			sql1 = "select password from user where account = ?";
			ps = con.prepareStatement(sql1);
			ps.setString(1, phone);
			rs = ps.executeQuery();
			if (rs.first())
				password = rs.getString("password");
		}catch (SQLException e) {
			e.printStackTrace();
			password = "";
		}finally{
			DBPool.close(con);
		}
		return password;
	}

	public static boolean isBlack(int userId, int otherId) {
		String sql1;
		ResultSet rs;
		Connection con = DBPool.getConnection();
		boolean isBlack = false;
		try {
			con.setAutoCommit(false);
			PreparedStatement ps;
			sql1 = "select EXISTS (select * from black where userid = ? and blackid = ?) exist";
			ps = con.prepareStatement(sql1);
			ps.setInt(1, userId);
			ps.setInt(2, otherId);
			rs = ps.executeQuery();
			if (rs.first())
				isBlack = rs.getInt("exist") > 0;
		}catch (SQLException e) {
			e.printStackTrace();
			isBlack = false;
		}finally{
			DBPool.close(con);
		}
		return isBlack;
	}

	public static int beBlack(int userId, int otherId, boolean black) {
		String sql0 = "use first_mysql_test";
		String sql1;
		if (black)
			sql1 = "insert into black (userid,blackid) values(?,?)";
		else
			sql1 = "delete from black where userid = ? and blackid = ?";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
			PreparedStatement ps;
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, otherId);
			ps.setInt(2, userId);
			ps.execute();
			con.commit();
			return 1;
		} catch (SQLException e) {
			try {
				System.out.println("插入数据库异常，正在进行回滚..");
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return -1;
		}finally{
			DBPool.close(con);
		}
	}

	public static int saveComplain(int userId, int zhencheng, int jingji, int chuangpin, int neihan, int shencai) {
		String sql0 = "use first_mysql_test";
		String sql1 = "update user set zhencheng = ?,jingji = ?,chuangpin = ?,neihan = ?,shencai = ? where id = ?";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
			PreparedStatement ps;
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, zhencheng);
			ps.setInt(2, jingji);
			ps.setInt(3, chuangpin);
			ps.setInt(4, neihan);
			ps.setInt(5, shencai);
			ps.setInt(6, userId);
			ps.execute();
			con.commit();
			return 1;
		} catch (SQLException e) {
			try {
				System.out.println("插入数据库异常，正在进行回滚..");
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return -1;
		}finally{
			DBPool.close(con);
		}
	}

	public static void insertToken(String account, String token) {
		String sql0 = "use first_mysql_test";
		String sql1 = "update user set token = ? where account = ?";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
			PreparedStatement ps;
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setString(1, token);
			ps.setString(2, account);
			ps.execute();
			con.commit();
		} catch (SQLException e) {
			try {
				System.out.println("插入数据库异常，正在进行回滚..");
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			DBPool.close(con);
		}
	}
}
