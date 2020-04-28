package com.qiqiim.webserver.util;

import org.apache.http.util.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MD5Util {
    public static String encryptPhone(String phoneStr){
        //1.换位号码中两组数字：第二位和第六位交换，第三位和第五位交换
        phoneStr=changeNum(phoneStr);
        //2.将全部号码转换为对应的字符
        phoneStr=getPhoneStr(phoneStr);
        //3.任意位置插入三个随机数字
        phoneStr=insertNum(phoneStr);
        //4.在步骤C之后的字符串前加上”~”
        phoneStr=prefixStr(phoneStr);
        return phoneStr;
    }

    /***
     * 换位号码中两组数字：第二位和第六位交换，第三位和第五位交换，位数号按从左往右数
     * 13510642584
     * 16015342584
     */
    public static String changeNum(String val){

        if(!TextUtils.isEmpty(val)){
            String str="";
            char [] phone=val.toCharArray();
            for(int i=0;i<phone.length;i++){
                if(i==1||i==2||i==4||i==5){
                    if(i==1)
                        str+=phone[5];
                    if(i==2)
                        str+=phone[4];
                    if(i==4)
                        str+=phone[2];
                    if(i==5)
                        str+=phone[1];
                }else{
                    str+=phone[i];
                }
            }
            return str;
        }
        return val;
    }

    /**
     * 数字 0 1 2 3 4 5 6 7 8 9
     * 字母 b a c g h k o w q p
     * @param val
     * @return 数字转字母
     */
    public static String getPhoneStr(String val) {
        String returnStr = "";
        if (val != null && val.length() > 0) {
            byte[] obj = val.getBytes();
            for (int i = 0; i < obj.length; i++) {
                returnStr += getLetter(String.valueOf(val.charAt(i)));
            }
        }
        return returnStr;
    }

    /**
     * 通过Map值获取key
     * @return
     */
    public static String getLetter(String val) {
        for(Map.Entry<String,String> entry:getMapData().entrySet()){
            if(val.equals(entry.getValue()))
                return String.valueOf(entry.getKey());
        }
        return val;
    }

    /**
     * 字母 b a c g h k o w q p
     * 数字 0 1 2 3 4 5 6 7 8 9
     * @return Map
     */
    public static Map<String,String> getMapData(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("b","0");
        map.put("a","1");
        map.put("c","2");
        map.put("g","3");
        map.put("h","4");
        map.put("k","5");
        map.put("o","6");
        map.put("w","7");
        map.put("q","8");
        map.put("p","9");
        return map;
    }

    /**
     * 随机插入3个数字
     */
    public static String insertNum(String val){
        String num=radomNum(3);
        //往指定长度字符串加入随机数字
        for(int i=0;i<num.length();i++){
            Integer inNum= Integer.parseInt(radomNum(1));
            String result=val.substring(0,inNum>val.length()?val.length():inNum)+num.charAt(i)+val.substring(inNum>val.length()?val.length():inNum,val.length());
            val=result;
        }
        return val;
    }

    /**
     * 随机生成i位数字
     * i<10
     */
    public static String radomNum(int i){
        Random random = new Random();
        String val=String.valueOf(random.nextInt());
        return val.substring(0,i).indexOf("-")>-1?radomNum(i):val.substring(0,i);
    }

    /**
     * 字符串前增加“~”
     */
    public static String prefixStr(String val){
        return "~"+val;
    }
}
