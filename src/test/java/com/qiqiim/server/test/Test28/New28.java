package com.qiqiim.server.test.Test28;

import com.qiqiim.webserver.user.controller.TokenUtil;
import com.qiqiim.webserver.util.AESUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

public class New28 {
    public static void main(String[] args) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String s = bufferedReader.readLine();
        String leftStr = s.substring(0, (s.length() + 1) / 2);
        long left = Long.parseLong(leftStr);
        long smaller = getLeftSmallerSymmetry(s.length(), left, leftStr.length());
        long bigger = getLeftBiggerSymmetry(s.length(), left, leftStr.length());
        long sValue = Long.parseLong(s);
        long sToSmaller=sValue - smaller;
        long sToBigger=bigger - sValue;
        long closestValue;
        long minDistance;
        if (sToSmaller < sToBigger) {
            closestValue = smaller;
            minDistance=sToSmaller;
            builder.append(closestValue);
        } else if (sToSmaller == sToBigger){
            closestValue = smaller;
            minDistance=sToSmaller;
            builder.append(sToSmaller).append(",").append(sToBigger);
        } else {
            closestValue = bigger;
            minDistance=sToBigger;
            builder.append(closestValue);
        }
        if (!isSymmetry(s)) {
            long equal = getLeftEqualSymmetry(s.length(), left, leftStr.length());
            long sToEqual = Math.abs(equal - sValue);
            if (sToEqual < minDistance) {
                closestValue = equal;
                builder = new StringBuilder(String.valueOf(closestValue));
            } else if (sToEqual == minDistance) {
                builder.append(",").append(equal);
            }
        }
        System.out.println(builder.toString());
    }

    //判断s是否对称
    static boolean isSymmetry(String s) {
        int mid = s.length() / 2;
        for (int i = 0, j = s.length() - 1; i < mid; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    //将左半部分反转到右半部分
    static void reverseLeftToRight(char[] cs) {
        int mid = cs.length / 2;
        for (int i = 0, j = cs.length - 1; i < mid; i++, j--) {
            cs[j] = cs[i];
        }
    }

    //获取左半部分减去1的对称值
    static long getLeftSmallerSymmetry(int originLen, long leftValue, int leftLen) {
        long leftSmaller = leftValue - 1;
        String leftSmallerStr = String.format("%0" + leftLen + "d", leftSmaller);
        char[] temp;
        temp = Arrays.copyOf(leftSmallerStr.toCharArray(), originLen);
        reverseLeftToRight(temp);
        if (temp[0] == '0' && temp.length > 1) {
            temp = String.valueOf(temp, 1, temp.length - 1).toCharArray();
            temp[temp.length - 1] = '9';
        }
        return Long.parseLong(String.valueOf(temp));
    }

    //获取左半部分相等的对称值
    static long getLeftEqualSymmetry(int originLen, long leftValue, int leftLen) {
        String leftEqualStr = String.valueOf(leftValue);
        char[] temp = Arrays.copyOf(leftEqualStr.toCharArray(), originLen);
        reverseLeftToRight(temp);
        return Long.parseLong(String.valueOf(temp));
    }

    /**
     * 获取左半部分加上1的对称值
     * @param originLen 原始字符串长度
     * @param leftValue 左半边的数值大小，101返回10
     * @param leftLen 左半边数值长度，101返回2
     */
    static long getLeftBiggerSymmetry(int originLen, long leftValue, int leftLen) {
        long leftBigger = leftValue + 1;
        String leftBiggerStr = String.valueOf(leftBigger);
        char[] temp;
        if (leftBiggerStr.length() > leftLen) {
            temp = Arrays.copyOf(leftBiggerStr.toCharArray(), originLen + 1);
        } else {
            temp = Arrays.copyOf(leftBiggerStr.toCharArray(), originLen);
        }
        reverseLeftToRight(temp);
        return Long.parseLong(String.valueOf(temp));
    }
}
