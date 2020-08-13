package com.qiqiim.constant;

public class TempArticle {
    public int id;//反馈id
    public int userId;//反馈作者id
    public int gender;//反馈作者性别
    public String title;//反馈标题
    public String content;//反馈内容
    public int pics;//反馈图片数量
    public int approve;//0:未审核,1:审核通过,-1:审核不通过
    public String reason;//未通过原因
    public int comment;//评论数

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getGender(){
        return gender;
    }
    public void setGender(int gender){
        this.gender = gender;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getPics() {
        return pics;
    }
    public void setPics(int pics) {
        this.pics = pics;
    }
    public int getApprove() {
        return approve;
    }
    public void setApprove(int approve) {
        this.approve = approve;
    }
    public int getComment() {
        return comment;
    }
    public void setComment(int comment) {
        this.comment = comment;
    }
}
