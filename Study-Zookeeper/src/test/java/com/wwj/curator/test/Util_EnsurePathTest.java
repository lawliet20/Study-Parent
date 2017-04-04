package com.wwj.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.EnsurePath;

/**
 * curator工具类测试
 * Created by sherry on 2016/12/15.
 * ensurepath提供了一种能够确保数据节点存在的机制。多用于这样的业务场景：
 * 上层业务希望对一个数据节点进行一些操作，但是操作之前需要确保该节点存在。
 */
public class Util_EnsurePathTest {
    private static String path = "/zk-book/c1";
    private static CuratorFramework client = ClientSingleton.getZkClient();

    public static void main(String[] args) throws Exception {
        ensurePathTest();
    }

    public static void ensurePathTest() throws Exception {
        client.start();
        EnsurePath ensurePath = new EnsurePath(path);
        ensurePath.ensure(client.getZookeeperClient());
        ensurePath.ensure(client.getZookeeperClient());

        EnsurePath ensurePath2 = client.newNamespaceAwareEnsurePath("/c1");
        ensurePath.ensure(client.getZookeeperClient());

    }


}
