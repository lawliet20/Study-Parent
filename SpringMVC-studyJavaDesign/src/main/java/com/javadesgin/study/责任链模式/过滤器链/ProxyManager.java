package com.javadesgin.study.责任链模式.过滤器链;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sun.security.provider.certpath.UntrustedChecker;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理管理器
 * Created by sherry on 2016/11/17.
 */
public class ProxyManager {

    @SuppressWarnings("unchecked")
    public static <T>T createProxy(final Class<?> targetClass,final List<Proxy> proxyList){
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass,o,method,methodProxy,objects,proxyList).doProxyChain();
            }
        });
    }
}
