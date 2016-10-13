package com.dubbo.service.Impl;

import java.util.ArrayList;
import java.util.List;

import com.dubbo.model.PageUser;
import com.dubbo.service.DemoService;

public class DemoServiceImpl implements DemoService {

    public String sayHello(String name) {
        return "Hello " + name;
    }

    public List<PageUser> getUsers() {
        List<PageUser> list = new ArrayList();
        PageUser u1 = new PageUser();
        u1.setName("jack");
        u1.setAge(20);
        u1.setSex("男");

        PageUser u2 = new PageUser();
        u2.setName("tom");
        u2.setAge(21);
        u2.setSex("女");

        PageUser u3 = new PageUser();
        u3.setName("rose");
        u3.setAge(19);
        u3.setSex("女");

        list.add(u1);
        list.add(u2);
        list.add(u3);
        System.out.println("调用dubboProvide2...");
        return list;
    }

}
