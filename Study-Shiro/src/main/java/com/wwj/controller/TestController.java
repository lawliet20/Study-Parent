package com.wwj.controller;

import com.wwj.model.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sherry on 16/10/8.
 */

@Controller("testController")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequiresPermissions("user:view")
    @RequestMapping("/test")
    public void login(User user,HttpServletRequest request,HttpServletResponse response){
        System.out.println("test.......");
    }
}