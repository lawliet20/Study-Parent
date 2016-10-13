package com.aop.javaProxy.dynamicProxy;

import com.aop.javaProxy.MessageHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by L on 2016/5/24.
 */
public class DynamicMessageProxy implements InvocationHandler {
    private static int count;
    private MessageHandler msgHandler;

    public DynamicMessageProxy(MessageHandler handler) {
        msgHandler = handler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("++++++++=============+++++++++");
        System.out.println("proxy:" + proxy.getClass());
        System.out.println("method:" + method);
        System.out.println("arg[0]:" + args[0].toString());
        System.out.println("++++++++=============+++++++++");
        if (args != null && args.length == 1 && checkMessage((String) args[0])) {
            count++;
            System.out.println("Message sent:" + count);
            return method.invoke(msgHandler, args);
        }
        return null;
    }

    private boolean checkMessage(String msg) {
        return msg != null && msg.length() > 10;
    }
}