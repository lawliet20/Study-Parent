package com.aop.javaReflection.jdkReflection;

/**
 * Created by L on 2016/5/26.
 */
class People {

    private String name = "张三";
    private int age;
    private static String like = "sherry";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}