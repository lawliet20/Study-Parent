package com.javadesgin.study.享元模式;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元工厂（单例模式）
 * Created by sherry on 2016/11/11.
 */
public class FlyWeightFactory {
    private static FlyWeightFactory flyWeightFactory = new FlyWeightFactory();
    private Map<Character,FlyWeight> files = new HashMap<Character,FlyWeight>();

    private FlyWeightFactory(){}

    public static FlyWeightFactory getInstance(){
        return flyWeightFactory;
    }

    public FlyWeight factory(Character state){
        if(files.containsKey(state)){
            return files.get(state);
        }else{
            FlyWeight fly = new ConcreteFlyWeight(state);
            files.put(state,fly);
            return fly;
        }
    }
}
