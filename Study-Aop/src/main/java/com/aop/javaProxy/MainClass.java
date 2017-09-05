package com.aop.javaProxy;

/**
 * Created by L on 2016/5/24.
 * 參考博客：http://blog.csdn.net/aaaaaaaa0705/article/details/6282793
 */
public class MainClass {
    private static void runProxy(MessageHandler handler) {
        handler.sendMessage("message for test");
    }

    public static void main(String[] args) {
        runProxy(new EmailMessage());
        System.out.println("++++++++++++++++Pjroxy++++++++++++++++++");
        runProxy(new MessageProxy());
    }
}