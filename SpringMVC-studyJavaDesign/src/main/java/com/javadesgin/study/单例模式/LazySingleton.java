package com.javadesgin.study.单例模式;

/**
 * 懒汉单例模式
 * Created by sherry on 2016/11/4.
 */
public class LazySingleton {

    private static LazySingleton lazySingleton = null;

    /**
     * 私有化构造方法
     */
    private LazySingleton(){

    }

    synchronized public static LazySingleton getSingleton(){
        if(null == lazySingleton){
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }

    public void about(){
        System.out.println("嗨，我是懒汉单例模式。");
    }
}
