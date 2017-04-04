package com.wwj.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 2016/11/16.
 */
public class Test {

    public static void main(String[] args) {
        String[] arr = new String[5];
        arr[0] = "0";
        arr[1] = "1";
        arr[2] = "2";
        arr[3] = "3";
        arr[4] = "4";
        String[] arr2 = new String[5];
        arr2[0] = "5";
        arr2[1] = "6";
        arr2[2] = "7";
        arr2[3] = "8";
        arr2[4] = "9";
        //System.out.println(arr.length);
        System.arraycopy(arr, 2, arr2, 0, 3);
        for (String str : arr2) {
            System.out.println(str);
        }

        Object obj = new Object();
        List list = new ArrayList<String>();
    }
}
