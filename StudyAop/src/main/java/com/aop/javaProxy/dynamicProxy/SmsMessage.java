package com.aop.javaProxy.dynamicProxy;

import com.aop.javaProxy.MessageHandler;

/**
 * Created by L on 2016/5/24.
 */
public class SmsMessage implements MessageHandler {
    @Override
    public void sendMessage(String msg) {
        System.out.println("SMS Message :" + msg + " sent !");
    }
}