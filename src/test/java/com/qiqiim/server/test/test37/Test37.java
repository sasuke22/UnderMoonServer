package com.qiqiim.server.test.test37;

public class Test37 {
    public static void main(String[] args){
        double[] result = generateTestValues(4,-1.00004,5.0006);
        for (double v : result) {
            System.out.println(v);
        }
    }

    public static double[] generateTestValues(int n, double a,double b){
        // a need to be smaller than b and n should greater than 0
        if (a > b || n < 0) throw new IllegalArgumentException("arguments are invalid!");

        double[] result = new double[n];
        int i = 0;
        // length of result should be equal to n
        while (i < n){
            // the value of Math.random is between 0 and 1,so we need to multiply with b-a,and then add a to make it between a and b
            result[i] = a + Math.random()*(b - a);
            i++;
        }
        return result;
    }
}
