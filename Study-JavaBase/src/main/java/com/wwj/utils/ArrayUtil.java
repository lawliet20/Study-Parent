package com.wwj.utils;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 数组工具类
 * Created by sherry on 16/9/23.
 * @since 1.0.0
 */
public class ArrayUtil {

    /**
     * 判断数组是否为非空
     */
    public static boolean isNotEmpty(Object[] array){
        return ArrayUtils.isNotEmpty(array);
    }

    /**
     * 判断数组是否为空
     */
    public static boolean isEmpty(Object[] array){
        return ArrayUtils.isEmpty(array);
    }
}
