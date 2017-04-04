package com.wwj.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 * Created by sherry on 16/9/4.
 *
 * @since 1.0
 */
public class StrUtil {

    /**
     * 字符串是空
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    /**
     * 字符串不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 字符串转大写
     */
    public static String toUpperCase(String str) {
        String res = null;
        if(isNotEmpty(str)){
            res = str.toUpperCase();
        }
        return res;
    }

    /**
     * 字符串转小写
     */
    public static String toLowerCase(String str) {
        String res = null;
        if(isNotEmpty(str)){
            res = str.toLowerCase();
        }
        return res;
    }


}
