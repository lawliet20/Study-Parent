package com.javadesgin.study.观察者模式_发布订阅模式;

import java.util.Observable;

/**
 * 被观察者
 * Created by sherry on 2016/11/20.
 */
public class Watched extends Observable{

    private String data = "";

    public String getData(){
        return data;
    }

    public void changeData(String data){
        if(!this.data.equals(data)){
            this.data = data;
            setChanged();
        }
        notifyObservers();
    }
}
