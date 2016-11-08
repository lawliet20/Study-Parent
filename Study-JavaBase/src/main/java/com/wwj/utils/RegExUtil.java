package com.wwj.utils;

import java.util.regex.Pattern;

/**
 * java正则工具类
 * Created by sherry on 2016/10/31.
 *
 * @since 1.0
 */
public class RegExUtil {

    public static void main(String[] args) {
        System.out.println(time("23:59:59"));


    }

    /**
     * 12小时制正则
     */
    public static boolean time12(String str) {
        String regex = "(([0][0-9])|([1][0-2])):[0-5][0-9]:[0-5][0-9]";
        return parse(regex, str);
    }

    /**
     * 24小时制正则
     */
    public static boolean time24(String str) {
        String regex = "(([0][0-9])|([1][0-9])|([2][0-4])):[0-5][0-9]:[0-5][0-9]";
        return parse(regex, str);
    }

    /**
     * 时间制正则
     */
    public static boolean time(String str) {
        return time12(str)|time24(str);
    }

    /**
     * qq邮箱正则
     */
    public static boolean qqEmail(String str){
        String regex = "\\d{4,}@qq\\.com$";
        return parse(str,regex);
    }

    /**
     * 邮箱正则
     */
    public static boolean email(String str){
        String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        return parse(str,regex);
    }

    /**
     * 解析字符串与正则是否匹配
     */
    public static boolean parse(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        boolean res = pattern.matcher(str).matches();
        return res;
    }
}
