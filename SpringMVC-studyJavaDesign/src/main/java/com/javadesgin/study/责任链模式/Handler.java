package com.javadesgin.study.责任链模式;

/**
 * Created by sherry on 2016/11/16.
 */
abstract public class Handler {
    private Handler nextHandler;
    private String name;

    public Handler(){

    }

    public Handler(String name){
        this.name = name;
    }

    /**
     * 商业逻辑
     */
    abstract public void operationRequest();

    public Handler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
