package com.wwj.test;

import com.wwj.com.wwj.threadLock.Service;
import com.wwj.com.wwj.threadLock.ThreadA;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by L on 2016/5/16.
 */
public class Test {

   private static ThreadLocal<Map<String,String>> sl = new ThreadLocal<Map<String, String>>(){
        @Override
        protected Map<String,String> initialValue(){
            Map<String,String> threadMap = new HashMap<String,String>();
            return threadMap;
        }
    };

    public static void main(String[] args) {
        Service s1 = new Service();
        Service s2 = new Service();
        Service s3 = new Service();
        ThreadA ta1 = new ThreadA(s1);
        ThreadA ta2 = new ThreadA(s2);
        ThreadA ta3 = new ThreadA(s3);
        Thread t1 = new Thread(ta1,"线程1");
        Thread t2 = new Thread(ta2,"线程2");
        Thread t3 = new Thread(ta3,"线程3");
        t1.start();
        t2.start();
        t3.start();
    }
}
