package com.javadesgin.study.抽象工厂模式.product;

/**
 * 产品cpu
 * Created by sherry on 2016/11/4.
 */
public class CPU extends Mainboard {

    @Override
    public void getName() {
        System.out.println("我是产品cpu");
    }
}
