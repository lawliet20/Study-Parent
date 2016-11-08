package com.javadesgin.study.策略模式;

/**
 * Created by sherry on 2016/11/7.
 */
public class Context {

    public static void main(String[] args) {
        DiscountStrategy d = DiscountFactory.getDiscount("打折");
        DiscountDecorator discountDecorator = new DiscountDecorator(d);
        double res = discountDecorator.discount(2000);
        System.out.println(res);
    }
}
