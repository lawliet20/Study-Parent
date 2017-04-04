package com.javadesgin.study.代理模式.cglib动态代理;

import net.sf.cglib.proxy.MethodProxy;

/**
 * Created by sherry on 2016/11/20.
 */
public class Test {
    private MethodProxy methodProxy;
    private Object targetClass;
    private Object[] params;

    public Test(MethodProxy methodProxy, Object targetClass, Object... params) {
        this.methodProxy = methodProxy;
        this.targetClass = targetClass;
        this.params = params;
    }

    public String test() {
        try {
            System.out.println("im test begin ...");
            methodProxy.invokeSuper(targetClass, params);
            System.out.println("im test end ...");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return "test";
    }
}
