package com.qiqiim.constant;

import java.util.Date;

public class SubComment {
    private int floorId;//层主评论id，对应meetingcomments或者articlecomments的id
    private int userId;//子评论用户id
    private String userName;//子评论用户名字
    private String content;//评论内容
    private Date date;//评论日期
    private int replyId;//回复谁的id
    private String replyName;//回复谁的名字
    private boolean showName;//是否显示名字
    private boolean sheShowName;//回复的评论者是否显示名字
    private boolean isVip;//年会员
    private boolean bigVip;//至尊会员
    private int commentGender;//性别

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

	public boolean isShowName() {
		return showName;
	}

	public void setShowName(boolean showName) {
		this.showName = showName;
	}

	public boolean isSheShowName() {
		return sheShowName;
	}

	public void setSheShowName(boolean sheShowName) {
		this.sheShowName = sheShowName;
	}

	public boolean isVip() {
        return isVip;
    }

    public void setIsVip(boolean vip) {
        isVip = vip;
    }

    public boolean isBigVip() {
        return bigVip;
    }

    public void setBigVip(boolean bigVip) {
        this.bigVip = bigVip;
    }

    public int getCommentGender() {
        return commentGender;
    }

    public void setCommentGender(int commentGender) {
        this.commentGender = commentGender;
    }
}
