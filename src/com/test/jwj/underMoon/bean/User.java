/**
 * 文件名：User.java
 * 时间：2015年5月9日上午10:23:19
 * 作者：修维康
 */
package com.test.jwj.underMoon.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

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
	private Date birthday;
	private int gender; // 0代表女生 1代表男生
	private boolean isOnline;
	private String location;
	private byte[] photo;
	private int age;
	private String userBriefIntro;
	private int height;
	private int marry;
	private String job;
	private String figure;
	private String xingzuo;
	private String loveType;
	private String photoAddress;
	private int score;

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

	public int getMarry() {
		return marry;
	}

	public void setMarry(int marry) {
		this.marry = marry;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getFigure() {
		return figure;
	}

	public void setFigure(String figure) {
		this.figure = figure;
	}

	public String getXingzuo() {
		return xingzuo;
	}

	public void setXingzuo(String xingzuo) {
		this.xingzuo = xingzuo;
	}

	public String getLoveType() {
		return loveType;
	}

	public void setLoveType(String loveType) {
		this.loveType = loveType;
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
		this.birthday = birthday;
		this.gender = gender;
		this.photo = photo;
	}

	public User() {

	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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

	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", userName="
				+ userName + ", password=" + password + ", birthday="
				+ birthday + ", gender=" + gender + ", isOnline=" + isOnline
				+ ", location=" + location + ", age=" + age
				+ ", userBriefIntro=" + userBriefIntro + ", height=" + height
				+ ", marry=" + marry + ", job=" + job + ", figure=" + figure
				+ ", xingzuo=" + xingzuo + ", loveType=" + loveType
				+ ", friendList=" + friendList + "]";
	}
	
}
