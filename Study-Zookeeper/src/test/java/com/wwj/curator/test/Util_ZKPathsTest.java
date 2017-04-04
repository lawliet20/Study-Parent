package com.wwj.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * curator工具类测试
 * Created by sherry on 2016/12/15.
 * zkpaths提供了一些简单的api来构建znode路径、递归创建和删除节点等。
 */
public class Util_ZKPathsTest {
    private static String path = "/curator_zkpath_sample";
    private static CuratorFramework client = ClientSingleton.getZkClient();

    public static void main(String[] args) {
        zkPathTest();
        System.out.println(isExists(path));

    }

    public static void zkPathTest() {
        try {
            client.start();
            ZooKeeper zooKeeper = client.getZookeeperClient().getZooKeeper();
            //System.out.println(ZKPaths.fixForNamespace(path,"/sub"));
            System.out.println(ZKPaths.makePath(path,"ddd"));
            System.out.println(ZKPaths.getNodeFromPath(path+"/sub1"));

            ZKPaths.PathAndNode pn = ZKPaths.getPathAndNode((path+"/sub1"));
            System.out.println(pn.getPath());
            System.out.println(pn.getNode());

            String dir1 = path+"/child1";
            String dir2 = path+"/child2";

            ZKPaths.mkdirs(zooKeeper,dir1);
            ZKPaths.mkdirs(zooKeeper,dir2);
            System.out.println(ZKPaths.getSortedChildren(zooKeeper,path));

            ZKPaths.deleteChildren(zooKeeper, path, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isExists(String path){
        boolean res = false;
        try {
            Stat stat = client.checkExists().forPath(path);
            if(stat!=null){
                res = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
