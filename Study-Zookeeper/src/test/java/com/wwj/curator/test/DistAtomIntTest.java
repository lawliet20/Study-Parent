package com.wwj.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;

/**
 * 分布式计数器
 * Created by sherry on 2016/12/13.
 * 一般可用于网站访问量统计
 */
public class DistAtomIntTest {
    private static CuratorFramework client = ClientSingleton.getZkClient();
    private static String disAtomInt_path = "/curator_recipes_distatomicint_path";

    public static void main(String[] args) throws Exception {
        client.start();
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client, disAtomInt_path, new RetryNTimes(3, 1000));
        AtomicValue<Integer> rc = atomicInteger.add(1);
        System.out.println("result:" + rc.succeeded());
        System.out.println("result:" + rc.postValue());
        System.out.println("result:" + rc.preValue());
        System.out.println("result:" + rc.getStats());
        System.out.println("result:" + rc.toString());
    }

}
