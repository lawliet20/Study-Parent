package com.javadesgin.study.桥梁模式.飞机与制造商;

/**
 * Created by sherry on 2016/11/25.
 */
public class CargoPlane extends AirPlan {
    public CargoPlane(){

    }

    public CargoPlane(AirPlanMaker airPlanMaker){
        super(airPlanMaker);
    }

    @Override
    public void fly() {
        System.out.println("货机起飞...");
    }

    public void hi(){
        super.getAirPlanMaker().hi();
    }
}
