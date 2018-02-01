package com.wwj.curator.test;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * master选举(zk集群模式下才有此场景)
 * Created by sherry on 2016/12/5.
 * 分布式中经常会遇到这样一个场景：对于一个复杂的任务需要从集群中选择一台执行即可
 */
public class MasterTest {

    static String master_path = "/curator_recipes_master_path";
    static CuratorFramework client = ClientSingleton.getZkClient();

    public static void main(String[] args) throws Exception {
        testLeadSelector();
    }

    /**
     * 测试第一种选主
     */
    public static void testLeadLatch() {
        Thread t1 = new Thread(run3());
        t1.start();
    }

    /**
     * 测试第二中选主
     */
    public static void testLeadSelector() {
        Thread t1 = new Thread(run1());
        Thread t2 = new Thread(run2());
        t1.start();
        t2.start();
    }

    public static Runnable run1() {
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                leadSelector("1111");
            }
        };
        return runnable1;
    }

    public static Runnable run2() {
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                leadSelector("222");
            }
        };
        return runnable1;
    }

    public static Runnable run3() {
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                leadLatch("111");
            }
        };
        return runnable1;
    }

    /**
     * leadSelector模式的选主
     * leaderSelector.autoRequeue();就是自动抢，
     * 比如打印一个helloworld，第一个打印完，第2个打印，然后第3个打印。然后第一个再打印，大家一起抢。
     * 有点动态选举的味道。
     */
    public static void leadSelector(final String name) {
        LeaderSelector leaderSelector = null;
        try {
            if (client.isStarted()){
                System.out.println("client is already start");
            }else{
                client.start();
            }
            leaderSelector = new LeaderSelector(client, master_path, new LeaderSelectorListenerAdapter() {
                //curator会再竞争到master时触发，在执行完takeLeadership方法后释放Master权利，
                // 重新开始新一轮master选举
                @Override
                public void takeLeadership(CuratorFramework client) throws Exception {
                    System.out.println(name + "成为master角色");
                    System.out.println("处理业务逻辑...");
                    System.out.println("完成master权利，释放master权利");
                }
            });
            //自动抢
            leaderSelector.autoRequeue();
            leaderSelector.start();
            System.out.println("press entry/return to quite...");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(leaderSelector);
            CloseableUtils.closeQuietly(client);
        }
    }

    /**
     * 这种是有阻塞的，就是大家一起上，谁先上了，就一直阻塞着，直到方法执行完成。
     * 如果执行结束，那么其他的兄弟就选一个出来。我觉得这种适合主备，比如开2 个 job，一个挂了另一个就上。
     *
     * @注意：一旦不使用LeaderLatch了，必须调用close方法。因为只能通过close释放当前的领导权。
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

    /**
     * TODO 异常处理： LeaderLatch实例可以增加ConnectionStateListener来监听网络连接问题。 当 SUSPENDED 或 LOST 时, leader不再认为自己还是leader.当LOST 连接重连后 RECONNECTED,LeaderLatch会删除先前的ZNode然后重新创建一个.
     * LeaderLatch用户必须考虑导致leadershi丢失的连接问题。 强烈推荐你使用ConnectionStateListener。
     *
     * @注意：这里是单线程多个选主问题，在多线程中（集群项目中多个相同功能）
     */
    public static void leadLatchTest() {
        try {
            ConnectionStateListener listener = new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    System.out.println("监听器触发...");
                }
            };
            List<CuratorFramework> clients = Lists.newArrayList();
            List<LeaderLatch> examples = Lists.newArrayList();
            TestingServer server = new TestingServer();
            try {
                for (int i = 0; i < 10; ++i) {
                    CuratorFramework client = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
                    clients.add(client);
                    LeaderLatch example = new LeaderLatch(client, master_path, "Client #" + i);
                    //添加监听器
                    //example.addListener();
                    examples.add(example);
                    client.start();
                    example.start();
                }

                Thread.sleep(20000);

                LeaderLatch currentLeader = null;
                for (int i = 0; i < 10; ++i) {
                    LeaderLatch example = examples.get(i);
                    if (example.hasLeadership()) {
                        currentLeader = example;
                    }
                }
                System.out.println("current leader is " + currentLeader.getId());
                System.out.println("release the leader " + currentLeader.getId());
                //await尝试获取领导权，但是未必能上位。
                examples.get(0).await(2, TimeUnit.SECONDS);
                System.out.println("Client #0 maybe is elected as the leader or not although it want to be");
                System.out.println("the new leader is " + examples.get(0).getLeader().getId());

                System.out.println("Press enter/return to quit\n");
                new BufferedReader(new InputStreamReader(System.in)).readLine();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("Shutting down...");
                for (LeaderLatch exampleClient : examples) {
                    //一旦不使用LeaderLatch了，必须调用close方法。如果它是leader,会释放leadership， 其它的参与者将会选举一个leader。
                    CloseableUtils.closeQuietly(exampleClient);
                }
                for (CuratorFramework client : clients) {
                    CloseableUtils.closeQuietly(client);
                }
                CloseableUtils.closeQuietly(server);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
