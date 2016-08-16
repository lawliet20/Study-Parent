package com.study.threadLocal;

import java.sql.Connection;

/**
 * 本地线程保存变量测试
 * @author L
 * 2016年5月8日13:28:18
 * 参考网址：http://blog.csdn.net/abc19900828/article/details/39500981
 */

public class ThreadLocalTest {

	public static void main(String[] args) {
		Connection con = getConnection();
	}
	
	//使用ThreadLocal保存Connection变量
	private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>(){
		@Override
		protected Connection initialValue() {
			Connection con = null;
			try {
				System.out.println("从本地线程取数据...");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return con;
		};
	};
	
	public static Connection getConnection(){
		return connectionHolder.get();
	}
		
	public static void setConnection(Connection conn){
		connectionHolder.set(conn);
	}
}
