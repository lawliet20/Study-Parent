package com.aop.javaProxy;

/**
 * Created by L on 2016/5/24.
 */
public class MessageProxy implements MessageHandler {
    private static int count;
    private MessageHandler emailMsg;

    @Override
    public void sendMessage(String msg) {
        if (checkMessage(msg)) {
            if (emailMsg == null) emailMsg = new EmailMessage();
            count++;
            emailMsg.sendMessage(msg);
            System.out.println("Message sent:" + count);
        }
    }

    private boolean checkMessage(String msg) {
        return msg != null && msg.length() > 10;
    }
}