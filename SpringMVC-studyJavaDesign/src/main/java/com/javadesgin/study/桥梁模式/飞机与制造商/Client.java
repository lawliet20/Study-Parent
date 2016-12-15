package com.javadesgin.study.桥梁模式.飞机与制造商;

/**
 * 桥梁模式测试
 * Created by sherry on 2016/11/25.
 * 一个桥梁模式中的实现角色可以是另一个桥梁模式中的抽象角色
 */
public class Client {

    public static void main(String[] args) {
        AirPlanMaker bosing = new Bosing();
        AirPlan airPlan = new PassengerPlane(bosing);
        airPlan.fly();
        bosing.hi();
    }
}
