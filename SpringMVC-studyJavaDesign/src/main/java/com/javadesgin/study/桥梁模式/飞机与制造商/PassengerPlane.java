package com.javadesgin.study.桥梁模式.飞机与制造商;

/**
 * 旅客飞机
 * Created by sherry on 2016/11/25.
 */
public class PassengerPlane extends AirPlan{
    public PassengerPlane(){

    }

    public PassengerPlane(AirPlanMaker airPlanMaker){
        super(airPlanMaker);
    }

    @Override
    public void fly() {
        System.out.println("商务机起飞...");
    }

    public void hi(){
        super.getAirPlanMaker().hi();
    }
}
