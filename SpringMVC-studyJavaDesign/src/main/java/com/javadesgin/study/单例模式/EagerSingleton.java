package com.javadesgin.study.单例模式;

/**
 * 饿汉单例模式
 * Created by sherry on 2016/11/4.
 */
public class EagerSingleton {

    private static final EagerSingleton eagerSingleton = new EagerSingleton();

    /**
     * 私有化构造方法
     */
    private EagerSingleton(){

    }

    public static EagerSingleton getSingleton(){
        return eagerSingleton;
    }

    public void about(){
        System.out.println("嗨，我是饿汉单例模式");
    }


}
