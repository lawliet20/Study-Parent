package com.javadesgin.study.策略模式;

/**
 * 折扣策略
 * Created by sherry on 2016/11/3.
 */
abstract public class DiscountStrategy {

    /**
     * 折扣测试
     * @param price 商品价格
     * @return 折扣后价格
     */
    public abstract double discount(double price);
}
