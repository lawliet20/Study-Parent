package com.spring.study.security;

import com.spring.study.model.pageModel.UserInfo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * security用户登录成功后处理类
 */
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    // 增加登录日志
    private static Logger logger = LogManager.getLogger(AuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        System.out.println("用户登录成功处理。。。。");
        UserInfo userDetail = (UserInfo) authentication.getPrincipal();
        System.out.println("用户名：" + userDetail.getUserName());
        System.out.println("用户like：" + userDetail.getLike());
        authentication.getName();
        List<GrantedAuthority> list = (List<GrantedAuthority>) authentication.getAuthorities();
        list.stream().forEach(e-> System.out.println("###权限###"+e.getAuthority()));
        super.onAuthenticationSuccess(request, response, authentication);
    }

    @Override
    public void setRequestCache(RequestCache requestCache) {
        super.setRequestCache(requestCache);
    }


}
