package com.javadesgin.study.桥梁模式;

/**
 * 抽象角色
 * Created by sherry on 2016/11/25.
 */
abstract public class Abstraction {

    private Implementor implementor;

    /**
     * 某个商业逻辑
     */
    public void operation(){
        implementor.operation();
    }
}
