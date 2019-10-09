package com.qiqiim.constant;

import java.sql.Date;

public class CommentDetail {
	public int id;//主键
	public int commentId;//评论的邀约或者反馈id
	public int userId;//评论者的id
	public String commentName;//评论者的名字
	public int commentGender;//评论者的性别
	public String commentContent;//评论内容
  	public Date commentDate;//评论的日期
  	public int isComplained;//是否被举报
  	public boolean show;//是否匿名
    public boolean isVip;//评论者是否是会员
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getUserId(){
		return userId;
	}
	public void setUserId(int userId){
		this.userId = userId;
	}
	public String getCommentName() {
		return commentName;
	}
	public void setCommentName(String commentName) {
		this.commentName = commentName;
	}
	public int getCommentGender() {
		return commentGender;
	}
	public void setCommentGender(int commentGender) {
		this.commentGender = commentGender;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public Date getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}
	public int getIsComplained() {
		return isComplained;
	}
	public void setIsComplained(int isComplained) {
		this.isComplained = isComplained;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public boolean getIsVip() {
		return isVip;
	}
	public void setIsVip(boolean isVip) {
		this.isVip = isVip;
	}
  	
  	
}
