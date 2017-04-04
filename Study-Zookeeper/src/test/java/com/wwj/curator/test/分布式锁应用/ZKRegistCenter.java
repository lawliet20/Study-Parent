package com.wwj.curator.test.分布式锁应用;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * zk注册中心
 * Created by sherry on 2016/12/6.
 */
public class ZkRegistCenter {


    /**
     * 获取zk客户端
     */
    public static CuratorFramework getZkClient() {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181,localhost:2182,localhost:2183")
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        client.start();
        return client;
    }

}
