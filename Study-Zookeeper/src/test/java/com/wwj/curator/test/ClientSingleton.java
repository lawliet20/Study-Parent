package com.wwj.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * zk客户端单例
 * Created by sherry on 2016/12/6.
 */
public class ClientSingleton {

    private ClientSingleton() {

    }

    /**
     * Curator内部实现的几种重试策略:
     * ExponentialBackoffRetry:重试指定的次数, 且每一次重试之间停顿的时间逐渐增加.
     * RetryNTimes:指定最大重试次数的重试策略
     * RetryOneTime:仅重试一次
     * RetryUntilElapsed:一直重试直到达到规定的时间
     */
    private static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("localhost:2181,localhost:2182,localhost:2183")
            .sessionTimeoutMs(5000)
            //.namespace("/abc")
            //.canBeReadOnly(true)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static CuratorFramework getZkClient() {
        return client;
    }

    public void startClient() {
        if (!client.isStarted()) {
            client.start();
        }
    }

    public void closeClient() {
        //client.close();
        if (client.isStarted()) {
            CloseableUtils.closeQuietly(client);
        }
    }
}
