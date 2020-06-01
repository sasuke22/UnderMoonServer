package com.qiqiim.server.test.test37;

public class DVD extends Item {
	private int playingTime;

	public DVD(double price, String name, double discount, int playingTime) {
		super(price, name, discount);
		this.playingTime = playingTime;
	}

	public int getPlayingTime() {
		return playingTime;
	}

	public void setPlayingTime(int playingTime) {
		this.playingTime = playingTime;
	}

	public String toString() {
		return super.toString() + ", playingTime: " + playingTime + " second";
	}

	public static void main(String[] args) {
		Item dvd = new DVD(50, "Story", 0, 320);
		System.out.println(dvd);
	}
}
