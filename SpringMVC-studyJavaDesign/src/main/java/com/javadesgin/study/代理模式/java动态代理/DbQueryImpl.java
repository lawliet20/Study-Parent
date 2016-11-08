package com.javadesgin.study.代理模式.java动态代理;

/**
 * Created by sherry on 2016/11/8.
 */
public class DbQueryImpl implements DbQuery {

    @Override
    public void createConnection() {
        System.out.println("创建数据库连接...");
    }
}
