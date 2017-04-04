package com.javadesgin.study.责任链模式.过滤器链;

/**
 * Created by sherry on 2016/11/17.
 */
public class AspectProxy implements Proxy {

    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        before();
        Object object = proxyChain.doProxyChain();
        after();
        return object;
    }

    public void before(){

    }

    public void after(){

    }
}
