package com.javadesgin.study.代理模式.cglib动态代理;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by sherry on 2016/11/17.
 */
public class CglibProxy implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public Object getProxy(Class clazz) {
        //设置需要创建子类的类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        //通过字节码技术动态创建子类的实例
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("前置代理");
        //System.out.println("当前方法名："+method.getName());
        //System.out.println(methodProxy.getSuperName());
        //new Test(methodProxy, o, objects).test();
        Object obj = methodProxy.invokeSuper(o, objects);
        System.out.println("后置代理");
        return obj;
    }
}
