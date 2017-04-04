package com.javadesgin.study.责任链模式.过滤器链;

/**
 * Created by sherry on 2016/11/17.
 */
public interface Proxy {

    /**
     * 执行代理链
     */
    public Object doProxy(ProxyChain proxyChain) throws Throwable;
}
