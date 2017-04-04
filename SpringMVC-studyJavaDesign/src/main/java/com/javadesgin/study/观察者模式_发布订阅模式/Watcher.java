package com.javadesgin.study.观察者模式_发布订阅模式;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者(java支持观察者模式)
 * Created by sherry on 2016/11/20.
 */
public class Watcher implements Observer {

    public Watcher(Watched watched){
        watched.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("data change to:"+((Watched)o).getData());
        System.out.println("arg="+arg);
    }
}
