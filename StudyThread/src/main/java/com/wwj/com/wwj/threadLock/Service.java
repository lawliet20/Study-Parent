package com.wwj.com.wwj.threadLock;

/**
 * Created by L on 2016/5/17.
 */
public class Service {
    private static int num = 0;

    public void addNum() {
        synchronized (Service.class) {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + ":" + (num++));
            }
        }
    }
}
