package com.javadesgin.study.策略模式;

/**
 * 正常收费，无折扣
 * Created by sherry on 2016/11/3.
 */
public class Normal extends DiscountStrategy{

    @Override
    public double discount(double money) {
        System.out.println("正常收费无折扣");
        return money;
    }
}
