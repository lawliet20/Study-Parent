package com.wwj.curator.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * zk权限demo
 * Created by sherry on 2016/12/7.
 * 通过acl权限创建的节点也只能通过acl权限才能操作
 */
public class AclTest {

    public static void main(String[] args) throws Exception {
        CuratorFramework aclClient = getAclClient();
        aclClient.create().forPath("/ddd", "hello".getBytes());
        byte[] data = aclClient.getData().forPath("/wwj");
        aclClient.close();

//        Thread.sleep(3000);
//        CuratorFramework client = getClient();
//        byte[] data = client.getData().forPath("/wwj");
//        System.out.println("数据："+new String(data));
//        client.close();
    }


    /**
     * @描述：获取连接(带权限)
     */
    private static CuratorFramework getAclClient() {
        CuratorFramework client = null;

        if (client == null) {
            ACLProvider aclProvider = new ACLProvider() {
                private List<ACL> acl;

                //初始化操作
                private synchronized void init() {
                    if (acl == null) {
                        acl = new ArrayList<ACL>();
                        acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", "admin:admin")));
                    }
                }

                @Override
                public List<ACL> getDefaultAcl() {
                    if (acl == null) {
                        init();
                    }
                    return this.acl;
                }

                @Override
                public List<ACL> getAclForPath(String path) {
                    if (acl == null) {
                        init();
                    }
                    return this.acl;
                }
            };
            String scheme = "digest";
            byte[] auth = "admin:admin".getBytes();
            int connectionTimeoutMs = 5000;
            String connectString = "localhost:2181";
            byte[] defaultData = "默认数据".getBytes();
            int maxCloseWaitMs = 5000;
            String namespace = "testlock";
            RetryPolicy retryPolicy = new RetryNTimes(Integer.MAX_VALUE, 5000);
            CuratorFramework clientinit = CuratorFrameworkFactory.builder().aclProvider(aclProvider).authorization(scheme, auth)
                    .connectionTimeoutMs(connectionTimeoutMs).connectString(connectString)
                    .defaultData(defaultData).maxCloseWaitMs(maxCloseWaitMs).namespace(namespace)
                    .retryPolicy(retryPolicy).build();
            clientinit.start();
            client = clientinit;
        }
        return client;
    }

    /**
     * @描述：获取连接
     */
    private static CuratorFramework getClient() {
        CuratorFramework client = ClientSingleton.getZkClient();
        client.start();
        return client;
    }
}
