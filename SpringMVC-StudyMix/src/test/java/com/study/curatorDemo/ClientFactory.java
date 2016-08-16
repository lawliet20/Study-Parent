package com.study.curatorDemo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ClientFactory {

	//获取一个zk客户端
	public static CuratorFramework newClient(){
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString("localhost:2181")  
		        .sessionTimeoutMs(30000)  
		        .connectionTimeoutMs(30000)  
		        .canBeReadOnly(false)  
		        .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))  
		        .namespace("")  
		        .defaultData(null)  
		        .build();  
		return client;
	}
	
	public static CuratorFramework newClient(String address,String port){
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(address+":"+port)  
		        .sessionTimeoutMs(30000)  
		        .connectionTimeoutMs(30000)  
		        .canBeReadOnly(false)  
		        .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))  
		        .namespace("")  
		        .defaultData(null)  
		        .build();  
		return client;
	}
	
	public static CuratorFramework newClient(String address,String port,String nameSpace){
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(address+":"+port)  
		        .sessionTimeoutMs(30000)  
		        .connectionTimeoutMs(30000)  
		        .canBeReadOnly(false)  
		        .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))  
		        .namespace(nameSpace)  
		        .defaultData(null)  
		        .build();  
		return client;
	}
}
