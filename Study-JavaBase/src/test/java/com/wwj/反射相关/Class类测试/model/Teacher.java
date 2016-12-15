package com.wwj.反射相关.Class类测试.model;

/**
 * Created by sherry on 2016/10/24.
 */
public class Teacher extends People implements Interface2 ,Interface1{
    private String position;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
