package com.wwj.队列;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * java队列demo
 * Created by sherry on 2016/12/27.
 * 参考网址：http://blog.sina.com.cn/s/blog_566fd08d0101fwcr.html
 * http://blog.itpub.net/143526/viewspace-1060365/
 */
public class QueueTest {

    /**
     * ArrayBlockingQueue基于数组的阻塞队列实现
     */
    @Test
    public void arrayBlockingQueueTest() throws InterruptedException {
        //初始化指定队列的容量，是否需要公平性（默认是false，如果公平参数被设置true，等待时间最长的线程会优先得到处理（其实就是通过将ReentrantLock设置为true来 达到这种公平性的：即等待时间最长的线程会先操作））
        final BlockingQueue queue = new ArrayBlockingQueue(10, false);

        //生产者
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 12; i++) {
                        //System.out.println(i + "-队列插入结果：" + queue.offer("编号->" + i));
                        System.out.println("插入队列：" + "编号->" + i);
                        queue.put("编号->" + i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //消费者
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 12; i++) {
                        //System.out.println("返回队列中的元素："+queue.poll());
                        System.out.println("返回队列中的元素：" + queue.take());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        System.out.println("队列中现在的元素长度：" + queue.size());
    }

    /**
     * LinkedBlockingQueue 基于链表的阻塞队列
     *
     * @注意：如果构造一个LinkedBlockingQueue对象，而没有指定其容量大小， LinkedBlockingQueue会默认一个类似无限大小的容量（Integer.MAX_VALUE），
     * 这样的话，如果生产者的速度一旦大于消费者的速度，也许还没有等到队列满阻塞产生，系统内存就有可能已被消耗殆尽了。
     */
    @Test
    public void linkedBlockingQueueTest() {
        BlockingQueue queue = new LinkedBlockingDeque();
        for (int i = 0; i < 12; i++) {
            System.out.println(i + "-队列插入结果：" + queue.offer("编号->" + i));
        }
        System.out.println("=================");
        for (int i = 0; i < 12; i++) {
            System.out.println("返回队列中的元素：" + queue.poll());
        }
        System.out.println("队列中现在的元素长度：" + queue.size());
    }

    /**
     * PriorityBlockingQueue
     * 基于优先级的阻塞队列（优先级的判断通过构造函数传入的Compator对象来决定），
     * 但需要注意的是PriorityBlockingQueue并不会阻塞数据生产者，而只会在没有可消费的数据时，阻塞数据的消费者。
     * 因此使用的时候要特别注意，生产者生产数据的速度绝对不能快于消费者消费数据的速度，否则时间一长，会最终耗尽所有的可用堆内存空间。在实现PriorityBlockingQueue时，内部控制线程同步的锁采用的是公平锁。
     */
    @Test
    public void priorityBlockingQueueTest() {
        BlockingQueue queue = new PriorityBlockingQueue();
        for (int i = 0; i < 12; i++) {
            System.out.println(i + "-队列插入结果：" + queue.offer("编号->" + i));
        }
        System.out.println("=================");
        for (int i = 0; i < 12; i++) {
            System.out.println("返回队列中的元素：" + queue.poll());
        }
        System.out.println("队列中现在的元素长度：" + queue.size());
    }

    /**
     * SynchronousQueue 一种无缓冲的等待队列，类似于无中介的直接交易
     * 如果一方没有找到合适的目标，那么对不起，大家都在等待。
     */
    @Test
    public void synchronousQueueTest() {
        final BlockingQueue queue = new SynchronousQueue();

        //生产者
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 12; i++) {
                        //System.out.println(i + "-队列插入结果：" + queue.offer("编号->" + i));
                        System.out.println("插入队列：" + "编号->" + i);
                        queue.put("编号->" + i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //消费者
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 12; i++) {
                        //System.out.println("返回队列中的元素："+queue.poll());
                        System.out.println("返回队列中的元素：" + queue.take());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        System.out.println("队列中现在的元素长度：" + queue.size());
    }

    /**
     * DelayQueue（基于PriorityQueue来实现的）是一个存放Delayed 元素的无界阻塞队列，只有在延迟期满时才能从中提取元素。
     * 该队列的头部是延迟期满后保存时间最长的 Delayed 元素。如果延迟都还没有期满，则队列没有头部，
     * 并且poll将返回null。当一个元素的 getDelay(TimeUnit.NANOSECONDS) 方法返回一个小于或等于零的值时，
     * 则出现期满，poll就以移除这个元素了。此队列不允许使用 null 元素。
     */
    public void delayQueueTest() throws InterruptedException {
        //参考DelayTest
    }

}
