package com.qiqiim.server.test.test8;

public class LinkedList {
    public static final String END = "end";
    public String address;
    public int data;

    public LinkedList(String address, int data) {
        this.address = address;
        this.data = data;
    }

    public void deleteFirstEven(LinkedList list) throws Exception {
        if (list.address.equals(END))
            throw new Exception("no even number");
//        else if (list.data % )
    }
}
