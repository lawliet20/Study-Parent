package com.wwj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by sherry on 2017/4/5.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/login")
    public String login(){
        System.out.println("hi，我要开始登陆了...");
        return "index";
    }

    @RequestMapping("/logout")
    public String logout(){
        System.out.println("hi,我要退出了...");
        return "index";
    }

}
