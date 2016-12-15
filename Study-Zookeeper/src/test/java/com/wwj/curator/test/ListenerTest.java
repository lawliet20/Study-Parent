package com.wwj.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * zk监听器测试
 * Created by sherry on 2016/11/29.
 */
public class ListenerTest {

    private static final String PATH = "/crud";

    private static CuratorFramework client;

    public static void main(String[] args) throws Exception {
        ListenerTest zk = new ListenerTest();
        zk.initConnection();

        zk.setWatcher();
        //zk.setListener2();
        //zk.setChildrenListener();
        //zk.setNodeListener();
        //zk.setTreeListener();
        //zk.setCuratorWatcher();
    }


    /**
     * 一次性监听
     */
    public void setWatcher() throws Exception {
        client.start();
        //注册观察者，当节点变动的时候触发
        byte[] data = client.getData().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("监控：" + watchedEvent);
            }
        }).forPath(PATH);
        client.setData().forPath("/test2", "keke".getBytes());
        client.setData().forPath("/test2", "keke2".getBytes());
        client.setData().forPath("/test2", "keke3".getBytes());
        System.out.println(PATH + "节点数据:" + new String(data));
        client.close();
    }

    /**
     * 一次性监听
     * getdata的时候触发
     */
    public void setCuratorWatcher() throws Exception {
        CuratorWatcher watcher = new CuratorWatcher() {
            @Override
            public void process(WatchedEvent event) throws Exception {
                System.out.println("path:" + event.getPath());
                Watcher.Event.EventType eventType = event.getType();
                if (eventType == Watcher.Event.EventType.None) {
                    Watcher.Event.KeeperState state = event.getState();
                    System.out.println(state.toString());
                } else {
                    System.out.println(eventType.toString());
                }
            }
        };

        client.start();
        Stat stat = new Stat();
        byte[] data = client.getData().storingStatIn(stat).usingWatcher(watcher).forPath(PATH);
        System.out.println("version:"+stat.getVersion());
        System.out.println("data:"+new String(data));

        client.close();
    }

    /**
     * 一次性的监听操作,使用后就无法在继续监听了
     */
    public void setListener2() throws Exception {
        //对节点的增、删、改、查都会触发、且只会触发一次。同时在连接关闭时也会触发一次。
        CuratorListener curatorListener = new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println("监听：" + curatorEvent);
                if (CuratorEventType.CLOSING == curatorEvent.getType()) {
                    System.out.println("client will be closed!");
                } else {
                    System.out.println("curatorEvent type is " + curatorEvent.getType());
                    System.out.println("curatorEvent path is " + curatorEvent.getPath());
                }
            }
        };
        client.start();
        client.getCuratorListenable().addListener(curatorListener);

        client.create().forPath("/ddd", "ddd".getBytes());
        Thread.sleep(1000);
        client.getData().forPath("/ddd");
        Thread.sleep(1000);
        client.setData().forPath("/ddd", "fff".getBytes());
        Thread.sleep(1000);
        client.delete().forPath("/ddd");
        Thread.sleep(1000);
    }

    /**
     * Path Cache：监视一个路径下1）孩子结点的创建、2）删除，3）以及结点数据的更新。
     * 能监听所有的字节点 且是无限监听的模式 但是 指定目录下节点的子节点不再监听
     * 如果new PathChildrenCache(client, PATH, true)中的参数cacheData值设置为false，
     * 则示例中的event.getData().getData()、data.getData()将返回null，cache将不会缓存节点数据。
     * <p/>
     * 注意：这里连续的增删改需要有间隔、否则可能不能保证每次监听都被触发（具体原因不详）
     */
    public void setChildrenListener() throws Exception {

        ExecutorService pool = Executors.newCachedThreadPool();
        client.start();
        final PathChildrenCache cache = new PathChildrenCache(client, PATH, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        PathChildrenCacheListener childrenListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                ChildData data = event.getData();
                System.out.println("开始进行事件分析..." + new String(data.getData()));
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("CHILD_ADDED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        break;
                    case CHILD_REMOVED:
                        System.out.println("CHILD_REMOVED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        break;
                    case CHILD_UPDATED:
                        System.out.println("CHILD_UPDATED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        break;
                    default:
                        System.out.println("OTHER : " + data.getPath() + "  数据:" + new String(data.getData()));
                        break;
                }
            }
        };
        cache.getListenable().addListener(childrenListener, pool);

        client.create().forPath(PATH + "/test01", "01".getBytes());
        Thread.sleep(10);
        client.create().forPath(PATH + "/test02", "02".getBytes());
        Thread.sleep(10);
        client.setData().forPath(PATH + "/test01", "01_V2".getBytes());
        Thread.sleep(10);
        client.delete().forPath(PATH + "/test01");
        Thread.sleep(10);
        client.delete().forPath(PATH + "/test02");
        Thread.sleep(10);

        cache.close();
        client.close();

    }

    /**
     * 监控本节点的变化情况
     * 监听本节点的变化  节点可以进行修改操作、删除节点后会再次创建(空节点)
     */
    public void setNodeListener() throws Exception {
        //在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
        ExecutorService pool = Executors.newCachedThreadPool();
        client.start();
        //设置节点的cache,这是默认为false，如果为true则事件不触发
        final NodeCache nodeCache = new NodeCache(client, PATH);
        nodeCache.start();

        NodeCacheListener listener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                ChildData data = nodeCache.getCurrentData();
                if (null != data) {
                    System.out.println("节点数据：" + new String(nodeCache.getCurrentData().getData()));
                } else {
                    System.out.println("节点被删除!");
                }
            }
        };

        nodeCache.getListenable().addListener(listener, pool);

        nodeCache.getListenable().addListener(listener);
        client.create().creatingParentsIfNeeded().forPath(PATH, "01".getBytes());
        Thread.sleep(10);
        client.setData().forPath(PATH, "02".getBytes());
        Thread.sleep(10);
        client.delete().deletingChildrenIfNeeded().forPath(PATH);
        Thread.sleep(1000 * 2);
        nodeCache.close();
        client.close();
        System.out.println("OK!");
    }

    /**
     * 可以监控整个树上的所有节点，类似上面两种cache的组合。
     */
    public void setTreeListener() throws Exception {
        client.start();
        TreeCache cache = new TreeCache(client, PATH);
        cache.start();
        TreeCacheListener listener = new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData data = event.getData();
                if (data != null) {
                    switch (event.getType()) {
                        case NODE_ADDED:
                            System.out.println("NODE_ADDED : " + data.getPath() + "  数据:" + new String(data.getData()));
                            break;
                        case NODE_REMOVED:
                            System.out.println("NODE_REMOVED : " + data.getPath() + "  数据:" + new String(data.getData()));
                            break;
                        case NODE_UPDATED:
                            System.out.println("NODE_UPDATED : " + data.getPath() + "  数据:" + new String(data.getData()));
                            break;

                        default:
                            break;
                    }
                } else {
                    System.out.println("data is null : " + event.getType());
                }
            }
        };
        cache.getListenable().addListener(listener);
        client.create().creatingParentsIfNeeded().forPath(PATH,"O(∩_∩)O~".getBytes());
        client.create().creatingParentsIfNeeded().forPath(PATH+"/cache/test01/child01");
        client.setData().forPath(PATH+"/cache/test01", "12345".getBytes());
        client.delete().deletingChildrenIfNeeded().forPath(PATH);
        Thread.sleep(1000 * 2);
        cache.close();
        client.close();
        System.out.println("OK!");
    }


    /**
     * 初始化zk连接
     */
    public void initConnection() {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("localhost:2181")
                // 重连策略,每隔1一秒重试一次,最大重试次数3次
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(2000)
                .sessionTimeoutMs(4000)
                .canBeReadOnly(false)
//                .namespace("zkListener")
                .build();
        this.client = client;
    }

    /**
     * 关闭连接
     */
    public void close() {
        client.close();
    }

}
