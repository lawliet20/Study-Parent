package com.javadesgin.study.抽象工厂模式.product;

/**
 * 产品主板
 * Created by sherry on 2016/11/4.
 */
public class Mainboard  extends AbstractProduct{

    @Override
    public void getName() {
        System.out.println("我是主板");
    }
}
