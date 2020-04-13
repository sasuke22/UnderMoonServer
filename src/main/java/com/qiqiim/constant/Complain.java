package com.qiqiim.constant;

public class Complain {
	private int id;//唯一id
	private int pics;//图片数
	private int userId;//举报人id
	private int complainId;//被举报人id
	private String remark;//举报内容
	private String type;//举报类型
	private int zhencheng;//真诚人品
	private int jingji;//经济独立
	private int chuangpin;//床品体感
	private int neihan;//内涵素质
	private int shencai;//照片真实
	
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getComplainId() {
		return complainId;
	}
	public void setComplainId(int complainId) {
		this.complainId = complainId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public int getZhencheng() {
		return zhencheng;
	}

	public void setZhencheng(int zhencheng) {
		this.zhencheng = zhencheng;
	}

	public int getJingji() {
		return jingji;
	}

	public void setJingji(int jingji) {
		this.jingji = jingji;
	}

	public int getChuangpin() {
		return chuangpin;
	}

	public void setChuangpin(int chuangpin) {
		this.chuangpin = chuangpin;
	}

	public int getNeihan() {
		return neihan;
	}

	public void setNeihan(int neihan) {
		this.neihan = neihan;
	}

	public int getShencai() {
		return shencai;
	}

	public void setShencai(int shencai) {
		this.shencai = shencai;
	}
}
