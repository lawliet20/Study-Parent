package com.javadesgin.study.代理模式.cglib动态代理;

/**
 * Created by sherry on 2016/11/17.
 */
public class Client {

    public static void main(String[] args) {
        CglibProxy proxy = new CglibProxy();
        //通过生成子类的形式创建代理类
        DbQueryImpl dbQuery = (DbQueryImpl) proxy.getProxy(DbQueryImpl.class);
        dbQuery.createConnection();
        String str = dbQuery.querySql();
        System.out.println(str);
    }
}
