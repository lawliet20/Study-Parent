package com.wwj.队列;

/**
 * Created by sherry on 2016/12/27.
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(test());
    }

    public static String test(){
        String str = "abc";
        try {
            System.out.println(str);
            throw new Exception("test err");
        } finally {
            return "err";
        }
    }

}
