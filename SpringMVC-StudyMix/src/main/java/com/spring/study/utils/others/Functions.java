package com.spring.study.utils.others;

import org.springframework.util.CollectionUtils;

/**
 * 操作资源权限功能标签
 */
public class Functions {

    public static boolean funin(@SuppressWarnings("rawtypes") Iterable iterable, Object element) {
        if (iterable == null) {
            return false;
        }
        return CollectionUtils.contains(iterable.iterator(), element);
    }

    /**
     * 显示指定个数，后面以点号代替
     */
    public static String stringOmit(String str, int n) {
        if (str == null) {
            return "";
        }
        if (str.length() > n) {
            return str.substring(0, n) + "...";
        } else {
            return str;
        }
    }
}

