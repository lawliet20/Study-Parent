package com.wwj.controller;

import com.wwj.controller.base.BaseController;
import com.wwj.model.User;
import com.wwj.service.UserService;
import com.wwj.utils.ShiroSessionUtil;
import com.wwj.utils.constant.Constant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Created by sherry on 16/10/8.
 */
@Controller("loginController")
public class LoginController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/login")
    public String login(User user, HttpServletRequest request, HttpServletResponse response) {
        logger.info("开始登录");
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        //token.setRememberMe(true);
        try {
            currentUser.login(token);
            //登录成功后： 保存用户信息到session
            saveUserInfoInSession(request);
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            logger.info("用户名无效");
            return "login";
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            logger.info("密码错误");
            return "login";
        } catch (AuthenticationException e) {
            logger.info("其他AuthenticationException");
            e.printStackTrace();
            return "login";
        }
        return "success";
    }

}
