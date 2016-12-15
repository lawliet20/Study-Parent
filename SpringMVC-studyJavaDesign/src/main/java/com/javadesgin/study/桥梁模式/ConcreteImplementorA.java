package com.javadesgin.study.桥梁模式;

/**
 * 具体实现类A
 * Created by sherry on 2016/11/25.
 */
public class ConcreteImplementorA extends Implementor{

    @Override
    protected void operation() {
        System.out.println("具体实现A操作开始...");
    }
}
