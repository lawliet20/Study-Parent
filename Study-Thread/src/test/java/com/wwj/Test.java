package com.wwj;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by sherry on 2017/11/9.
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(0, 1, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(3));
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread:"+Thread.currentThread().getName());
            }
        });
        Thread.sleep(2000);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread:"+Thread.currentThread().getName());
            }
        });
    }
}
