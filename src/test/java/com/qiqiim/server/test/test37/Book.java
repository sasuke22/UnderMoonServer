package com.qiqiim.server.test.test37;

public class Book extends Item {
	private int numberOfPages;

	public Book(double price, String name, double discount, int Pages) {
		super(price, name, discount);
		this.numberOfPages = Pages;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public String toString() {
		return super.toString() + "numberOfPages: " + numberOfPages;
	}

	public static void main(String[] args) {
		Item dvd = new Book(50, "Love Story", 0, 320);
		System.out.println(dvd);
		Item dvd1 = new Book(50, "Love Story", 20, 320);
		System.out.println(dvd1);

		Item book = new Book(100, "Magic", 0, 500);
		System.out.println(book);
		Item book1 = new Book(100, "Magic", 40, 500);
		System.out.println(book1);
	}
}
