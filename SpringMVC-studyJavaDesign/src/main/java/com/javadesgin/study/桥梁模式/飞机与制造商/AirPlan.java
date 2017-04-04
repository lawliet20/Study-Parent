package com.javadesgin.study.桥梁模式.飞机与制造商;

/**
 * 飞机
 * Created by sherry on 2016/11/25.
 */
abstract public class AirPlan {
    public AirPlan(){

    }

    public AirPlan(AirPlanMaker airPlanMaker){
        this.airPlanMaker = airPlanMaker;
    }

    private AirPlanMaker airPlanMaker;

    abstract public void fly();

    public AirPlanMaker getAirPlanMaker() {
        return airPlanMaker;
    }

    public void setAirPlanMaker(AirPlanMaker airPlanMaker) {
        this.airPlanMaker = airPlanMaker;
    }
}
