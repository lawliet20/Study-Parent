package com.wwj.curator.test.应用场景.命名服务;

import com.wwj.curator.test.ClientSingleton;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.TimeUnit;

/**
 * 分布式系统全局ID
 * Created by sherry on 2016/12/18.
 */
public class SeqID {

    private static CuratorFramework client = ClientSingleton.getZkClient();

    private static String seqID = "/seqID";

    public static void main(String[] args) {
        client.start();
        testSeqID();
    }

    /**
     * TODO 通过创建有序的节点的方式创建全局ID
     */

    /**
     * 分布式全局唯一id(分布式锁实现方式一)
     *
     * @注意：这里不用创建默认的seqID节点
     */
    public static void createSeqID1() {
        InterProcessMutex shareLock = new InterProcessMutex(client, "/seqID_lock");
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client, seqID, new RetryNTimes(3, 1000));
        try {
            //boolean res = shareLock.acquire(1000, TimeUnit.MILLISECONDS);
            //if (res) {
                AtomicValue<Integer> rc = atomicInteger.add(1);
                System.out.println(rc.succeeded()+" "+Thread.currentThread().getName() + " seqID：" + rc.postValue());
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(shareLock.isAcquiredInThisProcess()){
                try {
                    shareLock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 分布式全局唯一id(分布式锁实现方式二)
     *
     * @注意：默认已经创建好了seqID节点
     */
    public static void createSeqID2() {
        InterProcessMutex shareLock = new InterProcessMutex(client, "/seqID_lock");
        try {
            boolean res = shareLock.acquire(1000, TimeUnit.MILLISECONDS);
            if (res) {
                byte[] oldData = client.getData().forPath(seqID);
                byte[] newData = update(oldData);
                client.setData().forPath(seqID, newData);
                System.out.println(Thread.currentThread().getName() + " obtain seq=" + new String(newData));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                shareLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static byte[] update(byte[] currentData) {
        String s = new String(currentData);
        int d = Integer.parseInt(s);
        d = d + 1;
        s = String.valueOf(d);
        return s.getBytes();
    }

    /**
     * 创建多个线程，测试多线程下是否有2个线程出现相同id
     */
    public static void testSeqID() {
        for (int i = 0; i < 50; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    createSeqID1();
                }
            };
            thread.start();
        }
    }
}
