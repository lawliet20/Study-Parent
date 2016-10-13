package com.study.testDemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 测试java队列
 *
 * @author L 2016年4月7日16:42:29 参考链接： http://blog.sina.com.cn/s/blog_566fd08d0101fwcr.html
 */
public class JavaQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue arrQueue = new ArrayBlockingQueue(10);
        //ConcurrentLinkedQueue arrQueue = new ConcurrentLinkedQueue();

        Thread t1 = new Thread(new setQueueClass(arrQueue));
        t1.start();
        Thread.sleep(3000l);
        for (int i = 0; i < 5; i++) {
            new Thread(new getQueueClass(arrQueue)).start();
        }
    }

    // 向队列中插入数据
    private static class setQueueClass implements Runnable {

        private BlockingQueue blockingQueue;

        public setQueueClass() {
        }

        ;

        public setQueueClass(BlockingQueue blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        ;

        @Override
        public void run() {
            while (true) {
                try {
                    for (int a = 0; a < 150; a++) {
                        try {
                            System.out.println("queue set 开始向queue中插入数据：" + a);
                            blockingQueue.put("queue set is : " + a);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("插入数据结束");
                    Thread.sleep(5000l);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 向队列中插入数据
    private static class getQueueClass implements Runnable {

        private BlockingQueue blockingQueue;

        public getQueueClass() {
        }

        ;

        public getQueueClass(BlockingQueue blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        ;

        @Override
        public void run() {
            while (true) {
                try {
                    //System.out.println("queue get 当前线程 : " + Thread.currentThread().getName());
                    String str = (String) blockingQueue.take();
                    System.out.println("queue get 当前queue值 : " + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

}
