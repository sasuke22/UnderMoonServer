package com.qiqiim.server.test.test37;

public class Item {
	private double price;
	private String name;
	private double discountPercentage;
	public Item(double price, String name, double discountPercentage) {
		this.price = price;
		this.name = name;
		this.discountPercentage = discountPercentage;
	}

	public double getPrice() {
		return price;
	}

	public double getDiscountPrice() {
		return price * discountPercentage / 100.0;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discount) {
		this.discountPercentage = discount;
	}

	public String toString() {
		return this.getClass().getName() + " price: $" + price + ", name: " + name + ", discount"
				+ discountPercentage + ", discount price: $" + this.getDiscountPrice();
	}

	public static void main(String[] args){
		Item old = new Item(99,"airpods",100);
		System.out.println(old.toString());
		old.setDiscountPercentage(90);
		System.out.println(old.toString());
	}
}
