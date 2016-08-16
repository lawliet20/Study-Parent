package com.study.curatorDemo;

import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class CuratorDemo {
	
	public static void main(String[] args) throws Exception {
		CuratorDemo cd = new CuratorDemo();
		CuratorFramework client = ClientFactory.newClient();
		client.start();
		//cd.setDataAsync(client,"/hello","abc");
		cd.create(client, "/hello", "def");
	}
	
	// 创建实例
	public CuratorFramework createClient(String connectionString) {
		ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
		return CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
	}

	// 创建实例
	public CuratorFramework createClient(String connectionString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
		return CuratorFrameworkFactory.builder().connectString(connectionString).retryPolicy(retryPolicy).connectionTimeoutMs(connectionTimeoutMs).sessionTimeoutMs(sessionTimeoutMs).build();
	}

	// 创建节点
	public void create(CuratorFramework client, String path, String payload) throws Exception {
		client.create().forPath(path, payload == null ? null : payload.getBytes());
	}

	// 创建临时节点
	public void createEphemeral(CuratorFramework client, String path, String payload) throws Exception {
		client.create().withMode(CreateMode.EPHEMERAL).forPath(path, payload == null ? null : payload.getBytes());
	}

	// 设置节点内容
	public void setData(CuratorFramework client, String path, String payload) throws Exception {
		client.setData().forPath(path, payload == null ? null : payload.getBytes());
	}

	// 异步设置节点内容
	public void setDataAsync(CuratorFramework client, String path, String payload) throws Exception {
		CuratorListener listener = new CuratorListener() {
			public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println("receive");
				System.out.println("event:"+event.getName());
			}
		};
		client.getCuratorListenable().addListener(listener);
		client.setData().inBackground().forPath(path, payload == null ? null : payload.getBytes());
	}

	// 异步设置节点内容，带回调的
	public void setDataAsyncWithCallback(CuratorFramework client, BackgroundCallback callback, String path, String payload) throws Exception {
		client.setData().inBackground(callback).forPath(path, payload == null ? null : payload.getBytes());
	}

	// 删除节点
	public void delete(CuratorFramework client, String path) throws Exception {
		client.delete().forPath(path);
	}

	// 删除节点
	public void guaranteedDelete(CuratorFramework client, String path) throws Exception {
		client.delete().guaranteed().forPath(path);
	}

	// 监听节点
	public void watchData(CuratorFramework client, String path) throws Exception {
		final NodeCache nc = new NodeCache(client, path);
		nc.start();

		nc.getListenable().addListener(new NodeCacheListener() {
			public void nodeChanged() throws Exception {
				System.out.println(nc.getCurrentData().getPath() + ":" + new String(nc.getCurrentData().getData()));
			}
		});

		TimeUnit.SECONDS.sleep(20);
		nc.close();
	}

	// 监听子节点
	public void watchChild(CuratorFramework client, String path) throws Exception {
		PathChildrenCache pcc = new PathChildrenCache(client, path, true);
		pcc.start();

		pcc.getListenable().addListener(new PathChildrenCacheListener() {
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				System.out.println(event.getData().getPath() + ":" + new String(event.getData().getData()));
			}
		});

		TimeUnit.SECONDS.sleep(20);
		pcc.close();
	}

	// 监听树
	public void watchTree(CuratorFramework client, String path) throws Exception {
		TreeCache tc = new TreeCache(client, path);
		//TreeCache tc = TreeCache.newBuilder(client, path).build();
		tc.start();

		tc.getListenable().addListener(new TreeCacheListener() {
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				System.out.println("nodePath:"+event.getData().getPath() + ";nodeData:" + new String(event.getData().getData()));
				System.out.println("操作类型:"+(event.getType()==Type.NODE_UPDATED?"是更新操作":"不是更新操作"));
				System.out.println();
			}
		});

		TimeUnit.SECONDS.sleep(20);
		tc.close();
	}
	
	//添加启动监听
	public void watchConnection(CuratorFramework client,ConnectionStateListener sonnectionStateListener){
		client.getConnectionStateListenable().addListener(sonnectionStateListener);
	}
	
}