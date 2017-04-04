package com.javadesgin.study.策略模式;

/**
 * 折扣策略简单工厂
 * Created by sherry on 2016/11/3.
 */
public class DiscountFactory {

    public static DiscountStrategy getDiscount(String type){
        DiscountStrategy discountStrategy = null;
        switch (type){
            case "无折扣":
                discountStrategy = new Normal();
                break;
            case "打折":
                discountStrategy =  new RebateDiscount(0.8d);
                break;
            case "返利":
                discountStrategy = new ReturnDiscount(300d,100d);
                break;
            default:
                return new Normal();
        }

        return discountStrategy;
    }
}
