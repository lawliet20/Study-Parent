package com.javadesgin.study.抽象工厂模式.product;

/**
 * IBM的cpu
 * Created by sherry on 2016/11/4.
 */
public class IBMCpu extends CPU{

    @Override
    public void getName() {
        System.out.println("我是IBM的cpu");
    }
}
