package com.javadesgin.study.代理模式.java动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理对象，计算程序执行前后耗时多少
 * Created by sherry on 2016/11/8.
 */
public class DbQueryProxy implements InvocationHandler {
    private Object delegate;

    public DbQueryProxy(Object delegate){
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("程序开始执行");
        long begin = System.currentTimeMillis();
        method.invoke(delegate,args);
        long end = System.currentTimeMillis();
        System.out.println("程序执行结束");
        System.out.println("程序执行了"+(end-begin)+"毫秒");
        return "test";
    }
}
