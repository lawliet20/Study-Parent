package com.wwj.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * master选举(zk集群模式下才有此场景)
 * Created by sherry on 2016/12/5.
 * 分布式中经常会遇到这样一个场景：对于一个复杂的任务需要从集群中选择一台执行即可
 */
public class MasterTest2 {

    static String master_path = "/curator_recipes_master_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("localhost:2181").connectString("localhost:2182").connectString("localhost:2183")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(run3());
        t1.start();
    }

    public static Runnable run3() {
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                leadLatch("222");
            }
        };
        return runnable1;
    }

    /**
     * 这种是有阻塞的，就是大家一起上，谁先上了，就一直阻塞着，直到方法执行完成。
     * 如果执行结束，那么其他的兄弟就选一个出来。我觉得这种适合主备，比如开2 个 job，一个挂了另一个就上。
     */
    public static void leadLatch(String name) {
        client.start();
        // 选举Leader 启动
        LeaderLatch latch = new LeaderLatch(client, master_path);
        try {
            latch.start();
            latch.await();
            if (latch.hasLeadership()) {
                System.out.println(name + "我启动了");
            }
            System.out.println("press entry/return to quite...");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("shutting down...");
            //这里关闭顺序先关闭latch，后关闭client否则会报错。
            CloseableUtils.closeQuietly(latch);
            CloseableUtils.closeQuietly(client);
        }
    }

}
