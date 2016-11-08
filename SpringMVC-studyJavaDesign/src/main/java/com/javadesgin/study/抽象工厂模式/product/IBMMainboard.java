package com.javadesgin.study.抽象工厂模式.product;

/**
 * IBM主板
 * Created by sherry on 2016/11/4.
 */
public class IBMMainboard extends Mainboard{

    @Override
    public void getName() {
        System.out.println("我是IBM主板");
    }
}
