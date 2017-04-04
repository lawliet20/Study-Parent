package com.javadesgin.study.责任链模式.过滤器链.filter;

import com.javadesgin.study.责任链模式.过滤器链.AspectProxy;

/**
 * Created by sherry on 2016/11/19.
 */
public class TimeFilter extends AspectProxy {

    public void before(){
        System.out.println("time filter before");
    }

    public void after(){
        System.out.println("time filter after");
    }
}
