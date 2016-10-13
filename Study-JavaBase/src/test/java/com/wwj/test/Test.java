package com.wwj.test;

import java.util.StringTokenizer;

/**
 * Created by sherry on 16/9/4.
 */
public class Test {

    public static void main(String[] args) {
        String str = "http://www.baidu.com hello,world name:wwj";
        StringTokenizer st = new StringTokenizer(str, ",:");
        String[] strs = str.split(":|,|//");
//        while (st.hasMoreElements()) {
//
//            System.out.println("token : " + st.nextToken());
//
//        }
        for(int i=0;i<strs.length;i++){
            System.out.println("### "+strs[i]);
        }
    }
}
