package com.javadesgin.study.策略模式;

/**
 * 返利折扣
 * Created by sherry on 2016/11/3.
 */
public class ReturnDiscount extends DiscountStrategy {

    private double moneyCondition = 0.0d;
    private double moneyReturn = 0.0d;

    public ReturnDiscount(double moneyCondition, double moneyReturn) {
        this.moneyCondition = moneyCondition;
        this.moneyReturn = moneyReturn;
    }


    @Override
    public double discount(double money) {
        double result = money;
        if (money >= moneyCondition) {
            result = money - Math.floor(money / moneyCondition) * moneyReturn;
        }
        return result;
    }
}
