package com.wwj.反射相关.Class类测试.test;

import com.wwj.反射相关.Class类测试.enums.Enum;

/**
 * Created by sherry on 16/9/4.
 */
public class Test {

    public static Enum e = Enum.COOL;

    public static void main(String[] args) {
        while (true) {

            test1();
        }
    }

    public static void test1() {
        switch (e) {
            case COOL:
                System.out.println(e.getName());
                e = Enum.COMFORT;
                break;
            case COMFORT:
                System.out.println(e.getName());
                e = Enum.HOT;
                break;
            case HOT:
                System.out.println(e.getName());
                break;
            default:
                e = Enum.COOL;
                break;
        }
    }


}
