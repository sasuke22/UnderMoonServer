package com.qiqiim.server.test;

public class TreeNode {
    private int value;
    private TreeNode Lchild;
    private TreeNode Rchild;

    public TreeNode(int value, TreeNode lchild, TreeNode rchild) {
        this.value = value;
        Lchild = lchild;
        Rchild = rchild;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public TreeNode getLchild() {
        return Lchild;
    }

    public void setLchild(TreeNode lchild) {
        Lchild = lchild;
    }

    public TreeNode getRchild() {
        return Rchild;
    }

    public void setRchild(TreeNode rchild) {
        Rchild = rchild;
    }
}
