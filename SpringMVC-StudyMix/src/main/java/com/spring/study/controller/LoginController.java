package com.spring.study.controller;

import com.spring.study.model.User;
import com.spring.study.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sherry on 2017/3/26.
 */
@Controller
@RequestMapping("/user-account")
public class LoginController {

    @Resource
    private UserService userService;

    @RequestMapping("/login")
    public String login(User user,HttpServletRequest request,HttpServletResponse response){
        //获取spring-security中用户信息
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getPrincipal();
//        System.out.println("用户名："+userDetails.getUsername());
//        System.out.println("用户like："+userDetails.getAuthorities());
        System.out.println("####login#####");
        return "index";
    }

    @RequestMapping("/logout")
    public String hi(HttpServletRequest request,HttpServletResponse response){
        System.out.println("######***#######");
        return "index";
    }

}
