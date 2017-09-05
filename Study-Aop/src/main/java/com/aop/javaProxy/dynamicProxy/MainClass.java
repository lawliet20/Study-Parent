package com.aop.javaProxy.dynamicProxy;

import com.aop.javaProxy.EmailMessage;
import com.aop.javaProxy.MessageHandler;

import java.lang.reflect.Proxy;

/**
 * Created by L on 2016/5/24.
 */
public class MainClass {
    private static void runProxy(MessageHandler handler) {
        handler.sendMessage("message for test");
    }

    public static void main(String[] args) {
        MessageHandler handler = new EmailMessage();
        runProxy(handler);
        MessageHandler proxy = (MessageHandler) Proxy.newProxyInstance(
                MessageHandler.class.getClassLoader(),
                new Class[]{MessageHandler.class}, new DynamicMessageProxy(handler));
        runProxy(proxy);
        System.out.println("**************************************");
        // 短信方式
        handler = new SmsMessage();
        runProxy(handler);
        proxy = (MessageHandler) Proxy.newProxyInstance(
                MessageHandler.class.getClassLoader(),
                new Class[]{MessageHandler.class},
                new DynamicMessageProxy(handler));
        runProxy(proxy);
    }
}