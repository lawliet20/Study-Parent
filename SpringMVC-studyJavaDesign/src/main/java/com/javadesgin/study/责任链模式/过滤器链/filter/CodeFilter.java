package com.javadesgin.study.责任链模式.过滤器链.filter;

import com.javadesgin.study.责任链模式.过滤器链.AspectProxy;

/**
 * Created by sherry on 2016/11/19.
 */
public class CodeFilter extends AspectProxy {

    @Override
    public void before(){
        System.out.println("code filter before");
    }

    @Override
    public void after(){
        System.out.println("code filter after");
    }
}
