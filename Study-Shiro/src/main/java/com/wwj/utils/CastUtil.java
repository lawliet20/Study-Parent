package com.wwj.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by sherry on 16/9/21.
 */
public class CastUtil {

    /**
     * 转为String类型(默认是空)
     */
    public static String castStr(Object obj) {
        return castStr(obj, "");
    }

    /**
     * 转为String类型（可以指定默认值）
     */
    public static String castStr(Object obj, String defaultVal) {
        return obj == null ? defaultVal : obj.toString();
    }

    public static String castStr(char[] charArr) {
        return castStr(charArr, "");
    }

    public static String castStr(char[] charArr, String defaultStr) {
        String def = defaultStr;
        if (null != charArr) {
            def = new String(charArr);
        }
        return def;
    }

    /**
     * 转为int类型(默认返回0)
     */
    public static int castInt(Object obj) {
        return castInt(obj, 0);
    }

    /**
     * 转为int类型（可以指定默认返回值）
     */
    public static int castInt(Object obj, int defaultVal) {
        int value = defaultVal;
        String strObj = castStr(obj);

        if (!StringUtils.isEmpty(strObj)) {
            try {
                value = Integer.parseInt(strObj);
            } catch (NumberFormatException e) {
                value = defaultVal;
            }
        }
        return value;
    }

    /**
     * 转成 boolean 类型（默认返回false）
     */
    public static boolean castBoolean(Object obj) {
        return castBoolean(obj, false);
    }

    /**
     * 转成boolean类型（可以指定默认返回值）
     */
    public static boolean castBoolean(Object obj, boolean defaultVal) {
        boolean value = defaultVal;
        String strObj = castStr(obj);

        if (StringUtils.isNotEmpty(strObj)) {
            try {
                value = Boolean.parseBoolean(strObj);
            } catch (Exception e) {
                value = defaultVal;
            }
        }
        return value;
    }
}
