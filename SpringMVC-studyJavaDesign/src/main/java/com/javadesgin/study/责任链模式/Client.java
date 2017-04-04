package com.javadesgin.study.责任链模式;

/**
 * Created by sherry on 2016/11/16.
 */
public class Client {

    public static void main(String[] args) {
        ConcreteHandler handler1 = new ConcreteHandler("责任链1");
        ConcreteHandler handler2 = new ConcreteHandler("责任链2");
        ConcreteHandler handler3 = new ConcreteHandler("责任链3");

        handler1.setNextHandler(handler2);
        handler2.setNextHandler(handler3);

        handler1.operationRequest();
    }
}
