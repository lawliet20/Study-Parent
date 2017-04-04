package com.wwj.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingServer;
import org.apache.curator.test.TestingZooKeeperServer;

import java.io.File;

/**
 * curator提供了一种不需要安装zk服务器就可以进行测试的方法
 * Created by sherry on 2016/12/15.
 */
public class TestingServerTest {

    public static void main(String[] args) throws Exception {
//        singleMachineTest();
        clusterMachineTest();
    }

    /**
     * 返回单机模式下zk服务器
     */
    public static void singleMachineTest() throws Exception {
        TestingServer server = new TestingServer(2191,new File("/tmp/zookeeper"));
        server.start();
        CuratorFramework client = CuratorFrameworkFactory.builder()
                                .connectString(server.getConnectString())
                                .sessionTimeoutMs(5000)
                                .connectionTimeoutMs(3000)
                                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                                .build();
        client.start();
        client.create().forPath("/wwj","cool".getBytes());
        String str = new String(client.getData().forPath("/wwj"));
        System.out.println(str);
        server.close();
    }

    /**
     * @描述：模拟一个由3台机器组成的zk集群，同时在运行期间，将leader服务器kill掉。
     * 从程序运行的输出结果中可以看到，在leader被kill掉后，其他两台机器重新进行了leader选举。
     */
    public static void clusterMachineTest() throws Exception {
        TestingCluster cluster = new TestingCluster(3);
        cluster.start();
        Thread.sleep(2000);

        TestingZooKeeperServer leader = null;
        for(TestingZooKeeperServer zs:cluster.getServers()){
            System.out.println(zs.getInstanceSpec().getServerId()+"-");
            System.out.println(zs.getQuorumPeer().getServerState()+"-");
            System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
            if(zs.getQuorumPeer().getServerState().equals("leading")){
                leader = zs;
            }
        }
        leader.kill();
        System.out.println("-- after leader kill:");
        for(TestingZooKeeperServer zs:cluster.getServers()){
            System.out.println(zs.getInstanceSpec().getServerId()+"-");
            System.out.println(zs.getQuorumPeer().getServerState()+"-");
            System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
        }

        cluster.stop();
    }
}
