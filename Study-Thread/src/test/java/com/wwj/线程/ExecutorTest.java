package com.wwj.线程;

import java.util.concurrent.*;

/**
 * java线程池demo
 * Created by sherry on 2016/12/8.
 *
 * @描述：严格意义上讲Executor并不是一个线程池，而只是一个执行线程的工具。真正的线程池接口是ExecutorService。 ThreadPoolExecutor是Executors类的底层实现。
 * @线程池作用：1、减少了创建和销毁线程的次数，每个工作线程都可以被重复利用，可执行多个任务 2、可以根据系统的承受能力，调整线程池中工作线线程的数目，防止因为因为消耗过多的内存，而把服务器累趴下(每个线程需要大约1MB内存，线程开的越多，消耗的内存也就越大，最后死机)
 * 参考网址：http://zy116494718.iteye.com/blog/1704344
 */
public class ExecutorTest {

    public static void main(String[] args) {
        //FixedThreadPoolTest();
        //singleThreadTest();
        //cachedThreadPoolTest();
        scheduledThreadPool();
    }

    /**
     * 固定大小线程
     */
    public static void FixedThreadPoolTest() {
        //创建固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        threadPoolExecute(executorService);
    }

    /**
     * 创建一个使用单个 worker 线程的 Executor，以无界队列方式来运行该线程。
     */
    public static void singleThreadTest() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        threadPoolExecute(executorService);
    }

    /**
     * 创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。
     */
    public static void cachedThreadPoolTest() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        threadPoolExecute(executorService);
    }

    /**
     * 延迟连接池
     */
    public static void scheduledThreadPool() {
        //创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行。
        //这里的3表示创建一个固定线程数为3的线程池。
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(3);
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();
        MyThread t3 = new MyThread();
        MyThread t4 = new MyThread();
        MyThread t5 = new MyThread();
        MyThread t6 = new MyThread();
        //将线程放入线程池中执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.schedule(t4, 10, TimeUnit.MILLISECONDS);
        pool.schedule(t5, 10, TimeUnit.MICROSECONDS);
        pool.schedule(t6, 10, TimeUnit.MICROSECONDS);

        pool.shutdown();

    }

    /**
     * 创建一个单线程执行程序，它可安排在给定延迟后运行命令或者定期地执行。
     */
    public static void singleScheduledThreadPool() {
        ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
        //TODO 同上
    }

    /**
     * 自定义线程池
     */
    public static void threadPoolExecutorTest() {
        //创建等待队列
        BlockingQueue bqueue = new ArrayBlockingQueue(20);
        //创建一个单线程执行程序，它可安排在给定延迟后运行命令或者定期地执行。
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 3, 2, TimeUnit.MILLISECONDS, bqueue);
        //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
        Thread t1 = new MyThread();
        Thread t2 = new MyThread();
        Thread t3 = new MyThread();
        Thread t4 = new MyThread();
        Thread t5 = new MyThread();
        Thread t6 = new MyThread();
        Thread t7 = new MyThread();
        //将线程放入池中进行执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        pool.execute(t6);
        pool.execute(t7);
        //关闭线程池
        pool.shutdown();

    }

    private static void threadPoolExecute(ExecutorService executorService) {
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();
        MyThread t3 = new MyThread();
        MyThread t4 = new MyThread();
        MyThread t5 = new MyThread();
        MyThread t6 = new MyThread();
        //将线程放入线程池中执行
        executorService.execute(t1);
        executorService.execute(t2);
        executorService.execute(t3);
        executorService.execute(t4);
        executorService.execute(t5);
        executorService.execute(t6);
        //关闭线程池
        executorService.shutdown();
    }

    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "->执行...");
        }
    }


}
