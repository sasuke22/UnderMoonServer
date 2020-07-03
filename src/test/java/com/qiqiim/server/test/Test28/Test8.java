package com.qiqiim.server.test.Test28;

public class Test8 {
    public static void main(String[] args){

    }

    private static void quickSort(int[] arr,int low,int high){
        // if low is not smaller than high,do nothing
        if (low >= high) return;
        int i,j,temp,t;
        i = low;
        j = high;
        temp = arr[low];
        while (i < j){
            // 先指针j移动
            while (arr[j] >= temp && i < j){
                j--;
            }
            // 再指针i移动
            while (arr[i] <= temp && i < j){
                i++;
            }
            // 走到这里证明j遇到比基准值小的数，i遇到比基准值大的数，需要交换两值
            if (i < j){
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }
        }
        // 跳出循环证明i和j碰头了, 需要交换基准值和i的位置
        arr[low] = arr[i];
        arr[i] = temp;

        // 接下来需要将基准值两边分别作为两个数组，再进行排序
        quickSort(arr,low,i-1);
        quickSort(arr,i+1,high);
    }
}
