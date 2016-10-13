package com.wwj.utils;

import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by sherry on 16/9/21.
 */
public class CollectionUtil {

    /**
     * 判断Collection是否为空
     */
    public static boolean isEmpty(Collection collection){
        return collection.isEmpty();
    }

    /**
     * 判断Collection是否非空
     */
    public static boolean isNotEmpty(Collection collection){
        return !collection.isEmpty();
    }

    /**
     * 判断map是否为空
     */
    public static boolean isEmpty(Map<?,?> map){
        return MapUtils.isEmpty(map);
    }

    /**
     * 判断map是否非空
     */
    public static boolean isNotEmpty(Map<?,?> map){
        return !isEmpty(map);
    }
}
