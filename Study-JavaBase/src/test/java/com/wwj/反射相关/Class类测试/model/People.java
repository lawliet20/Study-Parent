package com.wwj.反射相关.Class类测试.model;

import com.wwj.反射相关.Class类测试.annotation.ClazzAnnotation;
import com.wwj.反射相关.Class类测试.annotation.FieldAnnotation;
import com.wwj.反射相关.Class类测试.annotation.MethodAnnotation;

/**
 * Created by sherry on 2016/10/24.
 */
@ClazzAnnotation("my name is people")
public class People implements Interface2{

    public People(){

    }

    public People(String name,int age,char sex){
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    private People(String name){
        this.name = name;
    }

    @FieldAnnotation("my name is filed:name")
    public String desc;
    private String name;
    private int age;
    private char sex;
    private Teacher teacher;

    @MethodAnnotation("my name is method:getName()")
    public String getName() {
        return name;
    }

    private void sayHi(String name){
        System.out.println("hi girl "+name);
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

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    //##########################################
    public class Test{
        String test;
    }

    private class Test1{
        String test;
    }

    class test2{
        String test;
    }

    public interface test3{
        public String getName();
    }

    private interface test4{
        public String getName();
    }
}

