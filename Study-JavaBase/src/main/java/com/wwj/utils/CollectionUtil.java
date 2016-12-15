package com.wwj.utils;

import org.apache.commons.collections4.MapUtils;

import java.util.*;

/**
 * 集合工具类
 * Created by sherry on 2016/11/13.
 */
public class CollectionUtil {

    /**
     * 判断容器是否为空
     */
    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     * 判断容器不为空
     */
    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断map为空
     */
    public static boolean isEmpty(Map map) {
        return null == map || MapUtils.isEmpty(map);
    }

    /**
     * 判断map不为空
     */
    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }
}
