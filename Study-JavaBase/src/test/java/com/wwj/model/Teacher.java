package com.wwj.model;

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
