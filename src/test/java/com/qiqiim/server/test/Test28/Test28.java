package com.qiqiim.server.test.Test28;

import java.util.ArrayList;
import java.util.List;

public class Test28 {
    public static void main(String[] args){
        List<Integer> a = new ArrayList<>();
        List<Integer> b = new ArrayList<>();
        a.add(1);a.add(4);a.add(5);
        b.add(1);b.add(4);b.add(5);
        List<Integer> result = unique(a,b);
        for (Integer integer : result) {
            System.out.println(integer);
        }
    }

    static List<Integer> unique(List<Integer> a, List<Integer> b){
        // define two cursors which max value of i is the size of a,max value of j is the size of b
        int i = 0,j = 0;
        List<Integer> result = new ArrayList<>();
        while(i < a.size() || j < b.size()){
            // if i >= a.size(),we can add b.get(j) directly
            if (i >= a.size()){
                result.add(b.get(j));
                j++;
                continue;
            }
            // if j >= b.size(),we can add a.get(i) directly
            if (j >= b.size()){
                result.add(a.get(i));
                i++;
                continue;
            }

            // if a.get(i) < b.get(j),we can add a.get(i);
            if(a.get(i) < b.get(j)){
                result.add(a.get(i));
                i++;
                continue;
            }
            // if a.get(i) > b.get(j),we can add b.get(j);
            if (a.get(i) > b.get(j)){
                result.add(b.get(j));
                j++;
                continue;
            }
            //if a.get(i) == b.get(j),we shouldn't add anything
            if (a.get(i).intValue() == b.get(j).intValue()){
                i++;
                j++;
            }
        }
        return result;
    }
}
