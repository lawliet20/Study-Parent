package com.javadesgin.study.状态模式;

/**
 * Created by sherry on 2016/11/2.
 */
public class StateImpl1 implements State {

    @Override
    public void operation1() {
        System.out.println("状态1：操作1");
    }

    @Override
    public void operation2() {
        System.out.println("状态1：操作2");
    }

    @Override
    public void operation3() {
        System.out.println("状态1：操作3");
    }
}
