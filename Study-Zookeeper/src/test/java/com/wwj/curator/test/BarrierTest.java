package com.wwj.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * zk分布式Barrier demo
 * Created by sherry on 2016/12/15.
 */
public class BarrierTest {

    private static CuratorFramework client = ClientSingleton.getZkClient();
    private static String barrier_path = "/curator_recipes_barrier_path";
    private static DistributedBarrier distributedBarrier;

    public static void main(String[] args) {
        barrierTest();
    }

    /**
     * 多线程就绪等待测试
     */
    public static void barrierTest() {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("localhost:2181,localhost:2182,localhost:2183")
                                .retryPolicy(new ExponentialBackoffRetry(1000, 3, 30000))
                                .connectionTimeoutMs(5000)
                                .sessionTimeoutMs(10000)
                                .canBeReadOnly(false)
                                .build();
                        client.start();
                        distributedBarrier = new DistributedBarrier(client, barrier_path);
                        System.out.println(Thread.currentThread().getName() + "号barrier设置");
                        distributedBarrier.setBarrier();
                        distributedBarrier.waitOnBarrier();
                        System.out.println("启动...");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        try {
            Thread.sleep(2000);
            distributedBarrier.removeBarrier();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
