package com.javadesgin.study.策略模式;

/**
 * 打折折扣
 * Created by sherry on 2016/11/3.
 */
public class RebateDiscount extends DiscountStrategy{
    //打几折？
    private double moneyRebate;

    public RebateDiscount(double moneyRebate){
        this.moneyRebate = moneyRebate;
    }

    @Override
    public double discount(double money) {
        System.out.println("打折折扣");
        return moneyRebate*money;
    }


}
