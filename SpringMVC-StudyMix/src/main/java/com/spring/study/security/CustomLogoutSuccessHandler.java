package com.spring.study.security;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户退出成功处理类
 */
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private static Logger logger = LogManager.getLogger(CustomLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        System.out.println("用户退出成功处理。。。。");
        String userName = authentication.getName(); //用户名
        String address = request.getRemoteAddr();  //远程地址
        logger.info("日志：ip:" + request.getRemoteAddr() + "host:" + request.getRemoteHost() + "退出时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        super.onLogoutSuccess(request, response, authentication);
    }


}
