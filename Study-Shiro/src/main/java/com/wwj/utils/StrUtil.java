package com.wwj.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by sherry on 16/10/3.
 */
public class StrUtil {

    public static boolean isEmpty(String str){
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
