package com.wwj.com.wwj.threadLock;

/**
 * Created by L on 2016/5/17.
 */
public class ThreadA implements Runnable {
    private Service service;

    public ThreadA(Service s) {
        this.service = s;
    }

    @Override
    public void run() {
        service.addNum();
    }
}
