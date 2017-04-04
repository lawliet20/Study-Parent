package com.wwj.反射相关.Class类测试.enums;

/**
 * 枚举用法
 * Created by sherry on 2016/10/25.
 */
public enum Enum {
    //RED, BLACK, BLUE, GRAY;

    COOL("冷",0),COMFORT("舒适",25),HOT("热",30);
    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    Enum(String name,int value){
        this.name=name;
        this.value = value;
    }
}

