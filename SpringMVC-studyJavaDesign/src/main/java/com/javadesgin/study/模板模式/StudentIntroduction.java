package com.javadesgin.study.模板模式;

/**
 * 参加工会的自我介绍
 * Created by sherry on 2016/10/31.
 */
public class StudentIntroduction extends AbstractIntroduction {
    @Override
    public void baseInfo() {
        System.out.println("大家好，我是w,今年大一。");
    }

    @Override
    public void interestIn() {
        System.out.println("我的兴趣爱好是：打球、运动、看书和上网。");
    }

    @Override
    public void otherDesc(){
        System.out.println("希望可以和大家做朋友。");
    }
}
