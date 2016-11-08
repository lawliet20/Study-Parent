package com.javadesgin.study.模板模式;

/**
 * 自我介绍（抽象）类
 * Created by sherry on 2016/10/31.
 */
abstract public class AbstractIntroduction {

    /**
     * 自我介绍
     */
    public void introductionSelf(){
        baseInfo();
        interestIn();
        otherDesc();
    }

    /**
     * 个人基本信息
     */
    abstract public void baseInfo();

    /**
     * 个人兴趣爱好
     */
    abstract public void interestIn();

    /**
     * 其他自我介绍（钩子方法）
     */
    public void otherDesc(){

    }

}
