package com.javadesgin.study.享元模式;

/**
 * Created by sherry on 2016/11/11.
 */
public class Client {

    public static void main(String[] args) {
        FlyWeightFactory factory = FlyWeightFactory.getInstance();
        //向享元工厂请求一个内蕴为a的享元对象
        FlyWeight fly = factory.factory(new Character('a'));
        //以参量的方式传入一个外蕴状态
        fly.operation("first call");

        fly = factory.factory(new Character('b'));
        fly.operation("second call");

    }
}
