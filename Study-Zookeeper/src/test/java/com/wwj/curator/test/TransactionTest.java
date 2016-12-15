package com.wwj.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * zk事物
 * Created by sherry on 2016/11/30.
 */
public class TransactionTest {

    private static final String PATH = "/crud";
    private static CuratorFramework client;

    public static void main(String[] args) throws Exception {
        TransactionTest zk = new TransactionTest();
        zk.initConnection();
        client.start();
        zk.test1();
        client.close();
    }

    /**
     * 初始化zk连接
     */
    public void initConnection() {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("localhost:2181")
                // 重连策略,没1一秒重试一次,最大重试次数3次
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(2000)
                .sessionTimeoutMs(10000)
                .canBeReadOnly(false)
                .build();
        this.client = client;
    }

    /**
     * 事物中的操作要么都成功、要么都失败
     */
    public void test1() throws Exception {
        CuratorTransaction tx = client.inTransaction();
        tx.delete().forPath(PATH+"/test/2")
                .and()
                .create().forPath(PATH+"/test/2","哇咔咔".getBytes())
                .and()
                .commit();
    }
}
