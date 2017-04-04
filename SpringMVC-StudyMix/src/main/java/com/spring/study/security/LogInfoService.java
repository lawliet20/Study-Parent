package com.spring.study.security;

import com.spring.study.model.pageModel.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by sherry on 2017/4/3.
 */
public class LogInfoService {

    public static String getLoginUserName() {
        //UserInfo userDetail = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //return userDetail.getUserName();
        return "wwj";
    }
}
