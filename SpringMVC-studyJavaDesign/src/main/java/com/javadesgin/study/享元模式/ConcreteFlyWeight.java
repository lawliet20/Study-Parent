package com.javadesgin.study.享元模式;

/**
 * 具体享元角色
 * Created by sherry on 2016/11/11.
 */
public class ConcreteFlyWeight extends FlyWeight {
    private Character intrinsicState = null;

    /**
     * 内蕴状态作为参量传入
     */
    public ConcreteFlyWeight(Character state){
        this.intrinsicState = state;
    }

    /**
     * 外蕴作为参量传入方法中，改变方法的行为
     * 但是不改变对象的内蕴状态
     */
    @Override
    public void operation(String state) {
        System.out.println("内蕴状态 intrinsicState："+intrinsicState+";" +
                "外蕴状态 state:"+state);
    }
}
