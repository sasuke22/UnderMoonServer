package com.qiqiim.constant;

public class Complain {
	private int id;//唯一id
	private int pics;//图片数
	private int userid;//举报人id
	private int complainid;//被举报人id
	private String remark;//举报内容
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPics() {
		return pics;
	}
	public void setPics(int pics) {
		this.pics = pics;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getComplainid() {
		return complainid;
	}
	public void setComplainid(int complainid) {
		this.complainid = complainid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
