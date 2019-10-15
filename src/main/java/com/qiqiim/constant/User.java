/**
 * 文件名：User.java
 * 时间：2015年5月9日上午10:23:19
 * 作者：修维康
 */
package com.qiqiim.constant;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 类名：User 说明：用户对象
 */
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String account;
	private String userName;
	private String password;
	private int gender; // 0代表女生 1代表男生
	private boolean isOnline;
	private String location;
	private int age;
	private String userBriefIntro;
	private int height;
	private String marry;
	private String job;
	private String xingzuo;
	private String registId;
	private String photoAddress;
	private int score;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date commentDate;
	private boolean show;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date vipDate;
	private int lock;
	
	public Date getVipDate() {
		return vipDate;
	}

	public void setVipDate(Date vipDate) {
		this.vipDate = vipDate;
	}

	public String getRegistId() {
		return registId;
	}

	public void setRegistId(String registId) {
		this.registId = registId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getPhotoAddress() {
		return photoAddress;
	}

	public void setPhotoAddress(String photoAddress) {
		this.photoAddress = photoAddress;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getMarry() {
		return marry;
	}

	public void setMarry(String marry) {
		this.marry = marry;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getXingzuo() {
		return xingzuo;
	}

	public void setXingzuo(String xingzuo) {
		this.xingzuo = xingzuo;
	}

	public String getUserBriefIntro() {
		return userBriefIntro;
	}

	public void setUserBriefIntro(String userBriefIntro) {
		this.userBriefIntro = userBriefIntro;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	private ArrayList<User> friendList;

	public ArrayList<User> getFriendList() {
		return friendList;
	}

	public void setFriendList(ArrayList<User> friendList) {
		this.friendList = friendList;
	}

	public User(String account, String username, String password,
			Date birthday, int gender, byte[] photo) {
		this.account = account;
		this.userName = username;
		this.password = password;
		this.gender = gender;
	}

	public User() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public boolean isOnline() {
		return isOnline;
	}
 
	public void setIsOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}
	
	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", userName="
				+ userName + ", password=" + password + ", gender=" + gender + ", isOnline=" + isOnline
				+ ", location=" + location + ", age=" + age
				+ ", userBriefIntro=" + userBriefIntro + ", height=" + height
				+ ", marry=" + marry + ", job=" + job
				+ ", xingzuo=" + xingzuo
				+ ", friendList=" + friendList + "]";
	}
	
}
