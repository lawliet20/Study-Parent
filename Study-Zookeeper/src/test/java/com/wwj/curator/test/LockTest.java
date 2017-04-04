package com.wwj.curator.test;

import org.apache.commons.lang3.RandomUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.framework.recipes.shared.SharedCount;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁demo
 * Created by sherry on 2016/12/7.
 * <p/>
 * zookeeper 锁功能的演示 基于curator
 * 共享锁(可重入/不可重入): 全局同步分布式锁, 同一时间两台机器只有一台能获得同一把锁.
 * 共享读写锁（可重入）: 用于分布式的读写互斥处理, 同时生成两个锁:一个读锁, 一个写锁,
 *          读锁能被多个应用持有, 而写锁只能一个独占, 当写锁未被持有时, 多个读锁持有者可以同时进行读操作
 * 共享信号量: 在分布式系统中的各个JVM使用同一个zk lock path,
 *         该path将跟一个给定数量的租约(lease)相关联, 然后各个应用根据请求顺序获得对应的lease,
 *         相对来说, 这是最公平的锁服务使用方式.
 * 多共享锁:内部构件多个共享锁(会跟一个znode path关联), 在acquire()过程中,
 *      执行所有共享锁的acquire()方法, 如果中间出现一个失败, 则将释放所有已require的共享锁;
 * 执行release()方法时, 则执行内部多个共享锁的release方法(如果出现失败将忽略)
 *
 * @注意：获取锁几次 释放锁也要几次
 */
public class LockTest {

    private static CuratorFramework client = ClientSingleton.getZkClient();
    private static String SHARE_LOK = "/lock/shareLock";
    private static String READ_WRITE_LOCK = "/lock/read_write_lock";
    private static String SHARE_SEMAPHORE_LOCK = "/lock/share_semaphore";
    private static String SEMAPHORE_LOCK = "/lock/semaphore";

    public static void main(String[] args) {
        client.start();
        //readWriteLockTest();
        testSharedLock();
//        for (int i = 0; i < 4; i++) {
//            sharedSemaphoreTest(i);
//        }
    }

    /*
    * 共享锁测试
    */
    private static void testSharedLock(){
        Thread t1 = new Thread("t1"){
            public void run(){
                shardLockTest();
            }
        };
        Thread t2 = new Thread("t2"){
            public void run(){
                shardLockTest();
            }
        };
        t1.start();
        t2.start();
    }

    /**
     * 共享锁
     * @注意：不应该在多个线程中用同一个InterProcessMutex，
     * 可以在每个线程中都生成一个InterProcessMutex实例，它们的path都一样，这样它们可以共享同一个锁。
     */
    public static void shardLockTest() {
//        下面两个都是共享锁，InterProcessMutex可重入锁，InterProcessSemaphoreMutex不可重入锁
//        两者的区别是：前者可以多次acquire获取锁，后者只能acquire获取一次，如果再次acquire获取锁则会则第二个锁出阻塞，直至超时。
//        new InterProcessMutex(client,LOCK_PATH);
//        new InterProcessSemaphoreMutex(client,LOCK_PATH);

        InterProcessMutex shareLock = new InterProcessMutex(client, SHARE_LOK);
        try {
            //尝试去获取共享锁，超过100毫秒未获取到则默认超时，不在操作。
            if (shareLock.acquire(100, TimeUnit.MILLISECONDS)) {
                System.out.println(Thread.currentThread().getName() + " get shareLock");
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //如果当前线程持有锁，则释放锁
                if (shareLock.isAcquiredInThisProcess()) {
                    System.out.println(Thread.currentThread().getName() + " release shareLock");
                    shareLock.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读写锁
     */
    public static void readWriteLockTest() {
        final InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(client, READ_WRITE_LOCK);
        final InterProcessMutex readLock = readWriteLock.readLock();
        final InterProcessMutex writeLock = readWriteLock.writeLock();

        List<Thread> jobs = new ArrayList<Thread>();

        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread("写锁 " + i) {
                public void run() {
                    readWriteLock(writeLock);
                }
            };
            jobs.add(thread);
        }

        for (int i = 0; i < 1; i++) {
            Thread thread = new Thread("读锁 " + i) {
                @Override
                public void run() {
                    readWriteLock(readLock);
                }
            };
            jobs.add(thread);
        }

        for (Thread thread : jobs) {
            thread.start();
        }
    }

    /**
     * 读写锁演示
     */
    public static void readWriteLock(InterProcessMutex lock) {
        System.out.println(Thread.currentThread().getName() + "进入任务");
        try {
            if (lock.acquire(100, TimeUnit.MILLISECONDS)) {
                int time = RandomUtils.nextInt(1000, 1000);
                System.out.println(Thread.currentThread().getName() + "执行读写任务开始");
                Thread.sleep(time);
                System.out.println(Thread.currentThread().getName() + "执行读写任务结束");
            } else {
                System.out.println(Thread.currentThread().getName() + "任务超时，未获取到锁。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println(Thread.currentThread().getName() + "  the flag is " + lock.isAcquiredInThisProcess());
                if (lock.isAcquiredInThisProcess()) {
                    lock.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 共享信号量
     */
    public static void sharedSemaphoreTest(final int x) {
        final InterProcessSemaphoreV2 semaphoreV2 = new InterProcessSemaphoreV2(client, SHARE_SEMAPHORE_LOCK,
                new SharedCount(client, SEMAPHORE_LOCK, 2));

        List<Thread> jobs = new ArrayList<Thread>();
        for (int i = 0; i < 2; i++) {
            final Thread thread = new Thread(x + "  共享信息锁  " + i) {
                public void run() {
                    sharedSemaphore(semaphoreV2);
                }
            };
            jobs.add(thread);
        }

        for (Thread thread : jobs) {
            thread.start();
        }
    }

    /**
     * 共享信号量
     * 设置总的数量 -->分布式情况下的最大并行数量
     * 按照请求顺序进行 执行权的分配
     * 可以设置超时 不执行 ;也可以设置 直到获取执行权 执行
     */
    public static void sharedSemaphore(InterProcessSemaphoreV2 semaphoreV2) {
        // new InterProcessSemaphoreV2(client, path, maxLeases)
        // new InterProcessSemaphoreV2(client, path, count)
        Lease lease = null;

        try {
            lease = semaphoreV2.acquire();
            if(null != lease){
                System.out.println(Thread.currentThread().getName()+" 任务开始");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName()+" 任务结束");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != lease){
                System.out.println("释放共享信息量  "+Thread.currentThread().getName());
                semaphoreV2.returnLease(lease);
            }
        }
    }

}
