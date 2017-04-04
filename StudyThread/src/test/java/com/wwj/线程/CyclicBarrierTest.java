package com.wwj.线程;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * java多线程同步demo
 * Created by sherry on 2016/12/13.
 * @描述：模拟比赛起跑
 * @参考网址：http://xijunhu.iteye.com/blog/713433
 */


public class CyclicBarrierTest {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("预备开始...");
            }
        });
        pool.submit(new Runner("张三",barrier));
        pool.submit(new Runner("李四",barrier));
        pool.submit(new Runner("王五",barrier));

        pool.shutdown();
    }

    static class Runner implements Runnable {
        private String name;
        CyclicBarrier barrier;// 计数器

        public Runner(String name, CyclicBarrier barrier) {
            this.name = name;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(name + "准备好了");
                barrier.await();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(name+" 起跑");
        }
    }

}
