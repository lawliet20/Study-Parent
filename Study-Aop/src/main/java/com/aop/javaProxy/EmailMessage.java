package com.aop.javaProxy;

/**
 * Created by L on 2016/5/24.
 */

//通过Email方式发送消息的实现类
public class EmailMessage implements MessageHandler {
    @Override
    public void sendMessage(String msg) {
        System.out.println(msg + " send!!");
    }
}

