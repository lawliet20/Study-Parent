package com.wwj.队列;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sherry on 2016/12/27.
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
        Map<String,String> map = new HashMap<String,String>();
        System.out.println(map.put("abc", "wwj"));
        System.out.println(map.put("abc", "wwj"));

        Thread.sleep(3000);
    }


}
