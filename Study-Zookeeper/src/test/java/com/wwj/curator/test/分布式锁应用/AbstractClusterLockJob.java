package com.wwj.curator.test.分布式锁应用;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * 执行前，需要抢锁的Job.
 *
 * @注意：前提： （1）每台机器定时任务时间应该一致。
 * （2）每台机器的时间应该大概一致。
 * （3）每个任务至少要运行1秒钟以上。
 * 另外一个重要点：
 * 最好能保证操作的幂等性，即可以重复操作多次，不影响。
 */
public abstract class AbstractClusterLockJob {

    /**
     * 锁路径.
     */
    private static final String LOCK_BASE_PATH = "/lock/timeJob/";

    /**
     * 执行入口.
     */
    public void execute() {
        CuratorFramework curatorClient = ZkRegistCenter.getZkClient();
        // 可重入共享锁
        InterProcessMutex sharedLock = new InterProcessMutex(curatorClient,
                LOCK_BASE_PATH + this.getLockName());
        try {
            // 尝试获取锁,并提供超时机制(超过1000ms未获取锁则认为获取失败)
            boolean isGetLock = sharedLock.acquire(1000, TimeUnit.MILLISECONDS);
            if (isGetLock) {
                this.executeTimeJob();
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            // Zookeeper错误或连接中断
            System.err.println("抢锁时出错");
            e.printStackTrace();
        } finally {
            try {
                // 持有锁，则释放锁
                if (sharedLock.isAcquiredInThisProcess()) {
                    sharedLock.release();
                }
            } catch (Exception e) {
                System.err.println("释放锁时出错");
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取锁名称.
     *
     * @return
     */
    protected abstract String getLockName();

    /**
     * 执行任务.
     */
    protected abstract void executeTimeJob();

}