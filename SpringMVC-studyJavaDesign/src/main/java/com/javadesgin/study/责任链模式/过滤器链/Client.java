package com.javadesgin.study.责任链模式.过滤器链;

import com.javadesgin.study.责任链模式.过滤器链.filter.CodeFilter;
import com.javadesgin.study.责任链模式.过滤器链.filter.RoleFilter;
import com.javadesgin.study.责任链模式.过滤器链.filter.TimeFilter;

import java.util.ArrayList;
import java.util.List;

import static com.javadesgin.study.责任链模式.过滤器链.ProxyManager.*;

/**
 * Created by sherry on 2016/11/19.
 */
public class Client {

    public static void main(String[] args) {
        TimeFilter timeFilter = new TimeFilter();
        CodeFilter codeFilter = new CodeFilter();
        RoleFilter roleFilter = new RoleFilter();
        List<Proxy> proxyList = new ArrayList<Proxy>();
        proxyList.add(timeFilter);
        proxyList.add(codeFilter);
        proxyList.add(roleFilter);

        Hello hello = ProxyManager.createProxy(Hello.class, proxyList);
        hello.hi();
    }

}
