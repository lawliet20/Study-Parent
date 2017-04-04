package com.javadesgin.study.代理模式.cglib动态代理;


/**
 * Created by sherry on 2016/11/8.
 */
public class DbQueryImpl {

    public void createConnection() {
        System.out.println("创建数据库连接...");
    }

    public String querySql(){
        System.out.println("查询数据库...");
        return "abc";
    }
}
