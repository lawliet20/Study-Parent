package com.javadesgin.study.策略模式;

/**
 * 对打完折的商品价格要求不超过1000元（装饰模式）
 * Created by sherry on 2016/11/7.
 */
public class DiscountDecorator extends DiscountStrategy{
    public DiscountStrategy discountStrategy;

    public DiscountDecorator(DiscountStrategy discountStrategy){
        this.discountStrategy = discountStrategy;
    }

    @Override
    public double discount(double price) {
        double lastPrice = discountStrategy.discount(price);
        if(lastPrice>1000){
            throw new RuntimeException("商品价格不能大于1000");
        }
        return lastPrice;
    }
}
