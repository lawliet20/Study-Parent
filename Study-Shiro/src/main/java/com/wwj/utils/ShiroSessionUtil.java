package com.wwj.utils;

import org.apache.shiro.SecurityUtils;

/**
 * shiro的session工具类
 * Created by sherry on 16/10/10.
 */
public final class ShiroSessionUtil {

    /**
     * 在session保存一条数据
     */
    public static <T> void setAttr(T key,T val){
        SecurityUtils.getSubject().getSession().setAttribute(key,val);
    }

    /**
     * 在session获取一条数据
     */
    public static Object getAttr(Object key){
       return SecurityUtils.getSubject().getSession().getAttribute(key);
    }

    /**
     * 在session删除一条数据
     */
    public static void delAttr(Object key){
        SecurityUtils.getSubject().getSession().removeAttribute(key);
    }

}
