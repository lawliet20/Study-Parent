package com.javadesgin.study.抽象工厂模式.product;

/**
 * Intel主板
 * Created by sherry on 2016/11/4.
 */
public class IntelMainboard extends Mainboard{

    @Override
    public void getName() {
        System.out.println("我是Intel主板");
    }
}
