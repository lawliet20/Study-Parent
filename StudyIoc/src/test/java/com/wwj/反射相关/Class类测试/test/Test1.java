package com.wwj.反射相关.Class类测试.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 16/9/15.
 */
public class Test1 {

    public static void main(String[] args) {
        List<String> list1 = new ArrayList<String>();
        list1.add("test1");

        List list2 = list1;
        list2.add("test2");

        System.out.println(list1.size());
        for(String str:list1){
            System.out.println(str);
        }
    }
}
