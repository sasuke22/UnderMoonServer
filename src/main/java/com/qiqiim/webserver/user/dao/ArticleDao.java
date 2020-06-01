package com.qiqiim.webserver.user.dao;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.qiqiim.constant.Article;
import com.qiqiim.constant.MeetingDetail;


public class ArticleDao {
	public static int addArticle(Article article,int pics){
		int res = 0;
		String sql0 = "use first_mysql_test";
		String sql1= "insert into articles (userId,gender,title,content,pics,approve,perfect) " +
				"values(?,?,?,?,?,?,?)";
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
			ps.setInt(1, article.userId);
			ps.setInt(2, article.gender);
			ps.setString(3, URLEncoder.encode(article.title,"utf-8"));
			ps.setString(4, URLEncoder.encode(article.content,"utf-8"));
			ps.setInt(5, pics);
			ps.setInt(6, 0);
			ps.setInt(7, 0);
			ps.execute();
			con.commit();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				res = rs.getInt(1);
			}
			DBPool.close(con);
			return res;
		} catch (Exception e) {
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				DBPool.close(con);
				e1.printStackTrace();
			}
			DBPool.close(con);
			e.printStackTrace();
			return -1;
		} finally{
			DBPool.close(con);
		}
	}
	
	public static ArrayList<Article> selectArticlesOrderByComments(int count){
		ArrayList<Article> articlesList = new ArrayList<Article>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * from articles " +
			      "where approve = 1 and islock = 0 order by comment desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, count);
			ps.setInt(2, 10);
			rs = ps.executeQuery();
			while (rs.next()) {
				Article article = new Article();
				article.setId(rs.getInt("id"));
				article.setUserId(rs.getInt("userId"));
				article.setGender(rs.getInt("gender"));
				article.setDate(new Date(rs.getTimestamp("date").getTime()));
				article.setTitle(URLDecoder.decode(rs.getString("title"),"utf-8"));
				article.setContent(URLDecoder.decode(rs.getString("content"),"utf-8"));
				article.setPics(rs.getInt("pics"));
				article.setApprove(rs.getInt("approve"));
				article.setComment(rs.getInt("comment"));
				articlesList.add(article);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			DBPool.close(con);
		}
		return articlesList;
	}
	
	public static ArrayList<Article> selectArticlesOrderByDate(int count){
		ArrayList<Article> articlesList = new ArrayList<Article>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * from articles " +
			      "where approve = 1 and islock = 0 order by date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, count);
			ps.setInt(2, 10);
			rs = ps.executeQuery();
			while (rs.next()) {
				Article article = new Article();
				article.setId(rs.getInt("id"));
				article.setUserId(rs.getInt("userId"));
				article.setGender(rs.getInt("gender"));
				article.setDate(new Date(rs.getTimestamp("date").getTime()));
				article.setTitle(URLDecoder.decode(rs.getString("title"),"utf-8"));
				article.setContent(URLDecoder.decode(rs.getString("content"),"utf-8"));
				article.setPics(rs.getInt("pics"));
				article.setApprove(rs.getInt("approve"));
				article.setComment(rs.getInt("comment"));
				articlesList.add(article);
			}
		} catch (Exception e) {
			DBPool.close(con);
		}
		DBPool.close(con);
		return articlesList;
	}

	/**
	 * 针对被针对的用户显示固定的邀约
	 */
	public static ArrayList<Article> getLockArticles(){
		ArrayList<Article> articlesList = new ArrayList<Article>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * from articles where approve = 1 and islock = 0 and comment = 0 order by date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, 0);
			ps.setInt(2, 10);
			rs = ps.executeQuery();
			while (rs.next()) {
				Article article = new Article();
				article.setId(rs.getInt("id"));
				article.setUserId(rs.getInt("userId"));
				article.setGender(rs.getInt("gender"));
				article.setDate(new Date(rs.getTimestamp("date").getTime()));
				article.setTitle(URLDecoder.decode(rs.getString("title"),"utf-8"));
				article.setContent(URLDecoder.decode(rs.getString("content"),"utf-8"));
				article.setPics(rs.getInt("pics"));
				article.setApprove(rs.getInt("approve"));
				article.setComment(rs.getInt("comment"));
				articlesList.add(article);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBPool.close(con);
		}
		return articlesList;
	}
	
	public static ArrayList<Article> getMyArticles(int userId,int count){
		ArrayList<Article> articlesList = new ArrayList<Article>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * from articles " +
			      "where userId = ? order by date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, userId);
			ps.setInt(2, count);
			ps.setInt(3, 10);
			rs = ps.executeQuery();
			while (rs.next()) {
				Article article = new Article();
				article.setId(rs.getInt("id"));
				article.setUserId(rs.getInt("userId"));
				article.setGender(rs.getInt("gender"));
				article.setDate(new Date(rs.getTimestamp("date").getTime()));
				article.setTitle(URLDecoder.decode(rs.getString("title"),"utf-8"));
				article.setContent(URLDecoder.decode(rs.getString("content"),"utf-8"));
				article.setPics(rs.getInt("pics"));
				article.setApprove(rs.getInt("approve"));
				article.setComment(rs.getInt("comment"));
				articlesList.add(article);
			}
		} catch (Exception e) {
			DBPool.close(con);
		}
		DBPool.close(con);
		return articlesList;
	}
	
	public static ArrayList<Article> selectPerfectArticles(int count){
		ArrayList<Article> articlesList = new ArrayList<Article>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from articles " +
			      "where perfect = 1 and islock = 0 order by date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, count);
			ps.setInt(2, 10);
			rs = ps.executeQuery();
			while (rs.next()) {
				Article article = new Article();
				article.setId(rs.getInt("id"));
				article.setUserId(rs.getInt("userId"));
				article.setGender(rs.getInt("gender"));
				article.setDate(new Date(rs.getTimestamp("date").getTime()));
				article.setTitle(URLDecoder.decode(rs.getString("title"),"utf-8"));
				article.setContent(URLDecoder.decode(rs.getString("content"),"utf-8"));
				article.setPics(rs.getInt("pics"));
				article.setApprove(rs.getInt("approve"));
				article.setComment(rs.getInt("comment"));
				articlesList.add(article);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			DBPool.close(con);
		}
		return articlesList;
	}
	
	public static int deleteArticle(int id){
		String sql0 = "use first_mysql_test";
		String sql1= "update articles set islock = 1 where id = ?";
		Connection con = DBPool.getConnection();
		int res;
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
			ps.setInt(1, id);
			ps.execute();
			con.commit();
			res = 1;
		} catch (SQLException e) {
			System.out.println("正在回滚");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				DBPool.close(con);
			}
			e.printStackTrace();
			res = -1;
		}finally{
			DBPool.close(con);
		}
		return res;
	}

	public static int reduceCommentCount(int commentId) {
		String sql0 = "use first_mysql_test";
		String sql1 = "update articles SET comment = comment - 1 where id = ? ";
		Connection con = DBPool.getConnection();
		int res;
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
			ps.setInt(1, commentId);
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
	
	public static int addCommentCount(int commentId) {
		String sql0 = "use first_mysql_test";
		String sql1 = "update articles SET comment = comment + 1 where id = ? ";
		Connection con = DBPool.getConnection();
		int res;
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
			ps.setInt(1, commentId);
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
	
	public static ArrayList<Article> selectAllArticlesByOldCount(int count){
		ArrayList<Article> articlesList = new ArrayList<Article>();
		String sq0 = "use first_mysql_test";
		String sql1 = "select * " +
			      "from articles " +
			      "order by approve asc,date desc limit ?,?" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, count);
			ps.setInt(2, 10);
			rs = ps.executeQuery();
			while (rs.next()) {
				Article article = new Article();
				article.setId(rs.getInt("id"));
				article.setUserId(rs.getInt("userId"));
				article.setGender(rs.getInt("gender"));
				article.setDate(new Date(rs.getTimestamp("date").getTime()));
				article.setTitle(URLDecoder.decode(rs.getString("title"),"utf-8"));
				article.setContent(URLDecoder.decode(rs.getString("content"),"utf-8"));
				article.setPics(rs.getInt("pics"));
				article.setApprove(rs.getInt("approve"));
				article.setComment(rs.getInt("comment"));
				articlesList.add(article);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			DBPool.close(con);
		}
		return articlesList;
	}
	
	public static int changeArticleApprove(Article article){
		String sql1;
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			PreparedStatement ps;
			if(article.getApprove() == 1){
				sql1 = "update articles SET approve = 1, title = ?,content = ? where id = ?";
				ps = con.prepareStatement(sql1);
				ps.setString(1, URLEncoder.encode(article.getTitle(),"utf-8"));
				ps.setString(2, URLEncoder.encode(article.getContent(),"utf-8"));
				ps.setInt(3, article.getId());
			}else{
				sql1 = "update articles SET approve = ?, reason = ? where id = ? ";
				ps = con.prepareStatement(sql1);
				ps.setInt(1, article.getApprove());
				ps.setString(2, article.getReason());
				ps.setInt(3, article.getId());
			}
			ps.executeUpdate();
			con.commit();
			return 1;
		}catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally{
			DBPool.close(con);
		}
	}

	public static int changeArticlePerfect(int id) {
		String sql1;
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			PreparedStatement ps;
			sql1 = "update articles a,user b SET perfect = 1,score = score + 15 where id = ? and a.userId = b.id";
			ps = con.prepareStatement(sql1);
			ps.setInt(1, id);
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

	public static Article getArticleDetail(int articleId) {
		String sq0 = "use first_mysql_test";
		String sql1 = "select id,userId,gender,date,title,content,pics,approve,comment from articles where id = ? limit 1" ;
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		Article detail = new Article();
		try {
			ps = con.prepareStatement(sq0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, articleId);
			rs = ps.executeQuery();
			while (rs.next()) {
				detail.setId(rs.getInt("id"));
				detail.setUserId(rs.getInt("userId"));
				detail.setGender(rs.getInt("gender"));
				detail.setDate(new Date(rs.getTimestamp("date").getTime()));
				detail.setTitle(URLDecoder.decode(rs.getString("title"),"utf-8"));
				detail.setContent(URLDecoder.decode(rs.getString("content"),"utf-8"));
				detail.setPics(rs.getInt("pics"));
				detail.setApprove(rs.getInt("approve"));
				detail.setComment(rs.getInt("comment"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			DBPool.close(con);
		}
		return detail;
	}
}
