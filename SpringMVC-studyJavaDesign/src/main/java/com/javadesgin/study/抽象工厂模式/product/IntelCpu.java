package com.javadesgin.study.抽象工厂模式.product;

/**
 * Intel的cpu
 * Created by sherry on 2016/11/4.
 */
public class IntelCpu extends CPU{

    @Override
    public void getName() {
        System.out.println("我是Intel的cpu");
    }
}
