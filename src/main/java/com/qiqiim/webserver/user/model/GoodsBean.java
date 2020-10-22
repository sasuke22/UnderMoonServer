package com.qiqiim.webserver.user.model;

public class GoodsBean {
	private int goodsId;
	private String goodsName;
	private String goodsPrice;
	private String goodsUrl;
	private int ownerId;
	
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getGoodsUrl() {
		return goodsUrl;
	}
	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}
	public int getOwnerId(){return ownerId;}
	public void setOwnerId(int ownderId) {
		this.ownerId = ownderId;
	}
}
