package com.javadesgin.study.抽象工厂模式.product;

/**
 * Created by sherry on 2016/11/4.
 */
abstract public class AbstractProduct implements Product {

    @Override
    public void createProduct() {
        System.out.println("产品的创建");
    }

    @Override
    public void userProduct() {
        System.out.println("产品的使用");
    }

    @Override
    public void destroyProduct() {
        System.out.println("产品的销毁");
    }
}
