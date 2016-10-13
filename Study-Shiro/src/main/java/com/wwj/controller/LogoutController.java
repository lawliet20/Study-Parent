package com.wwj.controller;

import com.wwj.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sherry on 16/10/8.
 */
@Controller("logoutController")
public class LogoutController {
    private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);

    @RequestMapping("/logout")
    public String login(User user,HttpServletRequest request,HttpServletResponse response){
        logger.info("开始退出");
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return "login";
    }
}
