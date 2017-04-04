package com.javadesgin.study.责任链模式;

/**
 * Created by sherry on 2016/11/16.
 */
public class ConcreteHandler extends Handler{
    
    public ConcreteHandler(){

    }

    public ConcreteHandler(String name){
        super(name);
    }

    @Override
    public void operationRequest() {
        if(null != getNextHandler()){
            System.out.println(getName()+"走向下一个责任链..."+getNextHandler().getName());
            getNextHandler().operationRequest();
        }else{
            System.out.println("处理简单商业逻辑..."+getName());
        }

    }
}
