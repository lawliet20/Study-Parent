/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.spring.study.utils.others.constants;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-2-7 下午5:14
 * <p>Version: 1.0
 */
public interface Constants {
    /**
     * 操作名称
     */
    String OP_NAME = "op";


    /**
     * 消息key
     */
    String MESSAGE = "message";

    /**
     * 错误key
     */
    String ERROR = "error";

    /**
     * 上个页面地址
     */
    String BACK_URL = "BackURL";

    String IGNORE_BACK_URL = "ignoreBackURL";

    /**
     * 当前请求的地址 带参数
     */
    String CURRENT_URL = "currentURL";

    /**
     * 当前请求的地址 不带参数
     */
    String NO_QUERYSTRING_CURRENT_URL = "noQueryStringCurrentURL";
    
    /**
     * 当前请求地址根目录（http://localhost:8080/ed-weixin）
     */
    String CURRENT_ROOTURL = "currentRootURL";

    /**
     * 项目名称
     */
    String CONTEXT_PATH = "ctx";
    
    

    /**
     * 当前登录的用户
     */
    String CURRENT_USER = "user";
    String CURRENT_USERNAME = "username";
    String CURRENT_MERCHANT_USER="merchant_key";
    String USERNO = "user_no";
    String AGENT_WX_OPENID = "agent_wx_openid";
    String AGENT_WX_ACCESSTOKEN = "agent_wx_accesstoken";
    String AGENT_WX = "agent_wx";
    
    /**
     * 当前扫一扫登录用户
     */
    String SCAN_PAY_USER = "scanPyaUser";
    String SCAN_PAY_MOBILE = "scanPyaMobile";
    
    /**
     * 当前登录的微信用户
     */
    String CURRENT_WXUSER = "wxuser";
    
    String WX_OPENID = "wx_openid";
    
    String WX_CODE = "wx_code";
    
    String USER_LOCATION = "user_location";
    
    String WX_ACCESS_TOKEN = "access_token";
    
    String WX_JSAPI_TICKET = "jsapi_ticket";
    
    /**
     * 商家进件申请信息
     */
    String MERCHANT_INFO ="merchantInfo";
    
    //保存时间
    String SAVE_TIME = "save_time";
    //失效时间
    String EXPIRES_IN = "expires_in";

    String ENCODING = "UTF-8";
    
    String REG_SMS_KEY = "reg_sms_key";//注册验证码key
    String REG_MOBILE_KEY = "reg_mobile_key";//注册手机号码key
    
    String ERROR_PW_KEY = "error_pw_key";//密码错误限制
    /*
     * 当前图片服务器路径
     * 当前图片访问路径
     */
    String IMG_ROOT="imgRoot";
    String IMG_URL="imgUrl";
    
    String SUBMIT_CODE_KEY = "submit_code_key";
    
    String PAY_NOTICE_KEY = "pay_notice_key";
}
