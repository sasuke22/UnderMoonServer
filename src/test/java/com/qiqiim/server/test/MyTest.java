package com.qiqiim.server.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.qiqiim.webserver.util.AESUtil;

import net.coobird.thumbnailator.Thumbnails;

public class MyTest {
	public static void main(String[] args) {
		String encrypt = AESUtil.encrypt("123456");
		System.out.println("encrypt:"+encrypt);
		String decrypt = AESUtil.decrypt(encrypt);
		System.out.println(decrypt);
	}
	
	public static String longestCommonPrefix(String[] strs){
		StringBuilder same = new StringBuilder();
		return same.toString();
	}
	
	/**
	 * 罗马转数字
	 */
	public static int romanToInt(String num){
		int sum = 0;
		int preNum = getValue(num.charAt(0));
		int secondNum = 0;
		for(int i = 1;i < num.length();i++){
			secondNum = getValue(num.charAt(i));
			if(preNum < secondNum){
				sum -= preNum;
			}else{
				sum += preNum;
			}
			preNum = secondNum;//i = num.length()时，这个等于chatAt(num.length())
		}
		//所以最后还要加一次
		sum += preNum;
		return sum;
	}
	
	private static int getValue(char ch){
		switch(ch){
			case 'M': return 1000;
			case 'D': return 500;
			case 'C': return 100;
			case 'L': return 50;
			case 'X': return 10;
			case 'V': return 5;
			case 'I': return 1;
			default: return 0;
		}
	}
	
	/**
	 * 转罗马数字 
	 */
	public static String intToRoman(int num){
		int[] nums = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
		String[] symbol = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
		StringBuilder builder = new StringBuilder();
		int index = 0;
		while(index < nums.length){
			while(num >= nums[index]){
				builder.append(symbol[index]);
				num -= nums[index];
			}
			index++;
		}
		return builder.toString();
	}
	
	/**
	 * 获取两数的最大面积 
	 */
	public static int maxArea(int[] height){
		int i = 0,j = height.length - 1,area = 0;
		while(i < j){
			if(height[i] > height[j])
				area = Math.max(area, (j-i)*height[j--]);
			else
				area = Math.max(area, (j-i)*height[i++]);
		}
		return area;
	}
	
	/**
	 * 回文数，数字反转（带符号）是否和原来相等 
	 */
	public static boolean isReverse(int x){
		//思考：这里大家可以思考一下，为什么末尾为 0 就可以直接返回 false
        if (x < 0 || (x % 10 == 0 && x != 0)) return false;
        int revertedNumber = 0;
        while (x > revertedNumber) {//这里是为了只反转后半段，还有如果是奇位数，会导致x>revertedNumber
            revertedNumber = revertedNumber * 10 + x % 10;
            x /= 10;
        }
        return x == revertedNumber || x == revertedNumber / 10;
	}
	
	/**
	 * 正则，不太对 
	 */
	public static void atoi(String str){
		String pattern = "^\\s*([+-]?\\d+)";
		Pattern r = Pattern.compile(str);
		Matcher m = r.matcher(pattern);
		if(m.find()){
			System.out.println(m.group(1));
		}else
			System.out.println("find nothing");
	}
	
	/**
	 * 整数反转，不能大于和小于int的最大值最小值 
	 */
	public static int reverse(int x){
		int ans = 0;
        while (x != 0) {
            int pop = x % 10;
            if (ans > Integer.MAX_VALUE / 10 || (ans == Integer.MAX_VALUE / 10 && pop > 7)) 
                return 0;
            if (ans < Integer.MIN_VALUE / 10 || (ans == Integer.MIN_VALUE / 10 && pop < -8)) 
                return 0;
            ans = ans * 10 + pop;
            x /= 10;
        }
        return ans;
	}
	
	public static String Ztrans(String s,int numRows){
		StringBuilder result = new StringBuilder();
		ArrayList<StringBuilder> list = new ArrayList(){};
		for(int i = 0;i < numRows;i++){
			list.add(new StringBuilder());
		}
		
		boolean dropDown = false;//方向
		int row = 0;
		for(int i = 0;i < s.length();i++){
			list.get(row).append(s.charAt(i));
			if(row == 0 || row == numRows - 1)
				dropDown = !dropDown;
			row += dropDown ? 1 : -1;
		}
		for(int i = 0;i < numRows;i++){
			result.append(list.get(i));
		}
		return result.toString();
	}
	
	/**
	 * 筛选出最长左右都相同的子字符串（回文字符串） 
	 */
	public static String findMaxSame(String s){
		int maxLen = 0;
		int start = 0,end = 0;
		for(int i = 1;i < s.length() - 1;i++){
			int len1 = centerExpand(s, i, i);
			int len2 = centerExpand(s, i, i+1);
			if(maxLen < Math.max(len1, len2)){
				maxLen = Math.max(len1, len2);
				start = i - (maxLen-1)/2;
				end = i + maxLen/2;
			}
		}
		return s.substring(start,end +1);
	}
	
	public static int centerExpand(String s,int left,int right){
		int i = left,j = right;
		while(i >= 0 && j < s.length() && s.charAt(i) == s.charAt(j)){
			i--;
			j++;
		}
		return j - i - 1;
	}
	
	public static int findX(int[] nums1,int[] nums2,int start1,int start2,int end1,int end2,int k){
		int n = nums1.length;
		int m = nums2.length;
		int need = (n + m)/2;
		int center = 0;
		if(n > m) return findX(nums2,nums1,end2,start2,start1,end1,k);
		if(k > 1){
			if(k > end1 - start1){//数组1的长度没了
				System.out.println(nums2[need-1]);
				center = (nums2[need-1] + nums2[need])/2;
				return center;
			}  
			if(nums1[k] > nums2[k]){//去除nums2的k之前的数
				center = findX(nums1,nums2,start1,nums1.length,k,nums2.length,k/2);
			}else if (nums1[k] < nums2[k]){//去除nums1的k之前的数
				center = findX(nums1,nums2,k,nums1.length,start2,nums2.length,k/2);
			}	
		}
		return center;
	}
	
	private static int getKth(int[] nums1, int start1, int end1, int[] nums2, int start2, int end2, int k) {
        int len1 = end1 - start1 + 1;
        int len2 = end2 - start2 + 1;
        //让 len1 的长度小于 len2，这样就能保证如果有数组空了，一定是 len1 
        if (len1 > len2) return getKth(nums2, start2, end2, nums1, start1, end1, k);
        if (len1 == 0) return nums2[start2 + k - 1];

        if (k == 1) return Math.min(nums1[start1], nums2[start2]);

        int i = start1 + Math.min(len1, k / 2) - 1;//试用math.min是防止出现k大于一个数组剩余长度的情况
        int j = start2 + Math.min(len2, k / 2) - 1;

        if (nums1[i] > nums2[j]) {
            return getKth(nums1, start1, end1, nums2, j + 1, end2, k - (j - start2 + 1));
        }
        else {
            return getKth(nums1, i + 1, end1, nums2, start2, end2, k - (i - start1 + 1));
        }
    }
}
