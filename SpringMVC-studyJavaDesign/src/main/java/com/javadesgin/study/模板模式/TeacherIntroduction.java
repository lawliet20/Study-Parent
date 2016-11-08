package com.javadesgin.study.模板模式;

/**
 *
 * Created by sherry on 2016/10/31.
 */
public class TeacherIntroduction extends AbstractIntroduction {


    @Override
    public void baseInfo() {
        System.out.println("大家好，我是你们的语文老师，sherry。");
    }

    @Override
    public void interestIn() {
        System.out.println("我的爱好是冒险。");
    }
}
