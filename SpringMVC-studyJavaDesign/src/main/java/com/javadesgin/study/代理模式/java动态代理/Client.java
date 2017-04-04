package com.javadesgin.study.代理模式.java动态代理;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * Created by sherry on 2016/11/8.
 */
public class Client {

    public static void main(String[] args) {
        DbQuery dbQuery = new DbQueryImpl();
        DbQueryProxy handler = new DbQueryProxy(dbQuery);
        DbQuery proxy = (DbQuery) Proxy.newProxyInstance(dbQuery.getClass().getClassLoader(), dbQuery.getClass().getInterfaces(), handler);
        proxy.createConnection();

    }
}
