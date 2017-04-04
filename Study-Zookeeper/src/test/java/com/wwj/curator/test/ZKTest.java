package com.wwj.curator.test;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 增删改查
 * Created by sherry on 2016/11/28.
 */
public class ZKTest {

    private static final String PATH = "/crud";

    private CuratorFramework client;

    @Before
    public void before(){
        initConnection();
    }

    @After
    public void flushRes(){
        if(null != client){
            client.close();
        }

    }

    /**
     * 初始化zk连接
     */
    public void initConnection() {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("localhost:2181,localhost:2182,localhost:2183")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3, 30000))
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(10000)
                .canBeReadOnly(false)
                //.namespace("zk1")
                .build();
        this.client = client;
        client.start();
    }

    /**
     * 新增节点
     */
    @Test
    public void createZnode() throws Exception {
        String nodeName2 = client.create().forPath(PATH, "hello world".getBytes());
        String nodeName1 = client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(PATH, "hello world".getBytes());
        System.out.println("nodeName1:"+nodeName1+";nodeName2:"+nodeName2);

        Stat stat = new Stat();
        byte[] data = client.getData().storingStatIn(stat).forPath(nodeName1);
        System.out.println("新增的节点数据为：" + new String(data)+";当前数据节点数据内容的版本号："+stat.getVersion());
        System.out.println("新增的节点数据为：" + new String(data)+";当前数据节点子节点的版本号："+stat.getCversion());
        System.out.println("新增的节点数据为：" + new String(data)+";当前数据节点ACL变更版本号："+stat.getAversion());
        //以距离时间原点(epoch)的毫秒数表示的znode创建时间
        System.out.println("新增的节点数据为：" + new String(data)+";znode创建时间："+stat.getCtime());
        System.out.println("新增的节点数据为：" + new String(data)+";znode最近修改时间："+stat.getMtime());
        System.out.println("新增的节点数据为：" + new String(data)+";创建节点的事务的zxid："+stat.getCzxid());
        System.out.println("新增的节点数据为：" + new String(data)+";对znode最近修改的zxid："+stat.getMzxid());
        System.out.println("新增的节点数据为：" + new String(data)+";父节点的zxid："+stat.getPzxid());
        System.out.println("新增的节点数据为：" + new String(data)+";数据长度："+stat.getDataLength());
        //如果该节点为ephemeral节点, 该值表示与该节点绑定的session id。如果不是ephemeral节点, ephemeralOwner值为0
        System.out.println("新增的节点数据为：" + new String(data)+";该节点的sessionID："+stat.getEphemeralOwner());
        System.out.println("新增的节点数据为：" + new String(data)+";子节点个数："+stat.getNumChildren());
    }

    /**
     * 创建临时节点
     */
    @Test
    public void createEphemeral() throws Exception {
        //创建一个临时znode，客户端断开后临时节点就删除。
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/wwj", "cool".getBytes());
        System.out.println("新增临时节点数据："+client.getData().forPath("/wwj"));
    }

    /**
     * 创建有序自增临时节点
     */
    @Test
    public void createEphemeralSequential() throws Exception {
        //创建一个有序的受保护的（节点在zk中是加密的密文）临时节点，客户端断开后临时节点删除
        client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/wwj", "cool...".getBytes());
    }

    /**
     * 异步创建节点
     */
    @Test
    public void setDataAsyncWithCallback() throws Exception {
        BackgroundCallback callback = new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework curatorFramework, CuratorEvent event) throws Exception {
                System.out.println("================");
                Long begin = (Long) event.getContext();
                Long end = System.currentTimeMillis();
                System.out.println("<Time>:" + (end - begin) + "ms");
                //event.getType();
                //event.getResultCode();
                //check result
                KeeperException.Code rc = KeeperException.Code.get(event.getResultCode());
                if (rc != KeeperException.Code.OK) {
                    System.out.println("Background operation fail,path:" + event.getPath() + ";Message:" + KeeperException.create(rc).getMessage());
                }
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        long current = System.currentTimeMillis();
        String value = "test-" + current;
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                //executor参数：在zk中所以的异步通知都是由eventhread线程管理的。EventThread的“串行处理”机制在
                //绝大数情况下可以很好的做到对事件处理的顺序性。但是也是有弊端的，如果前一个事件逻辑复杂，耗时较长则会影响
                //对其他事件的处理。因此在inBackground参数中允许传入一个executor参数,用一个单独的线程处理此事件。
                .inBackground(callback, executor)
                .forPath(PATH + "/test/1", value.getBytes("utf-8"));
        Thread.sleep(100);
    }

    /**
     * 更新节点
     */
    @Test
    public void updateZnode() throws Exception {
        System.out.println(PATH+"节点修改前："+new String(client.getData().forPath(PATH)));
        client.setData().forPath(PATH, "you are very good".getBytes());
        byte[] data = client.getData().forPath(PATH);
        System.out.println(PATH + "节点修改后：" + new String(data));
    }

    /**
     * 删除节点
     */
    @Test
    public void deleteZnode() throws Exception {
        client.delete().forPath(PATH);
        System.out.println("path节点是否存在：" + client.checkExists().forPath(PATH));
    }

    /**
     * 完全删除节点
     */
    @Test
    public void guaranteedDelete() throws Exception {
        //保证完整的删除给出的节点
        client.delete().guaranteed().forPath("/wwj");
    }

    /**
     * 查询子节点
     */
    @Test
    public void getChildren() throws Exception {
        List<String> list = client.getChildren().forPath("/");
        for (String str : list) {
            System.out.println("zookeeper节点下所有的子节点：" + str);
        }
    }

    /**
     * 查询根目录下所有的节点
     */
    @Test
    public void getAll() throws Exception {
        client.start();
        List<String> list = client.getChildren().forPath("/");
        queryChildren(list, new StringBuffer(""));
    }

    public void queryChildren(List<String> list, StringBuffer childPath) throws Exception {
        if (!CollectionUtils.isEmpty(list)) {
            for (String node : list) {
                StringBuffer clonePath = new StringBuffer(childPath);
                clonePath.append("/").append(node);
                List<String> childrens = client.getChildren().forPath(clonePath.toString());
                consoleNodeData(clonePath.toString());
                if (!CollectionUtils.isEmpty(childrens)) {
                    queryChildren(childrens, clonePath);
                }
            }
        }
    }

    /**
     * 打印zk对应节点和节点数据
     */
    public String consoleNodeData(String path) {
        String str = "";
        try {
            byte[] data = client.getData().forPath(path);
            str = new String(data);
            System.out.println("节点" + path + " 数据：" + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
