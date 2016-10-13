package com.javadesgin.study.Observer;

import java.util.Observable;
import java.util.Observer;

/**
 * 订阅发布模式（观察者）
 *
 * @author L 2016年5月2日12:35:48 观察者demo
 */
public class Watcher implements Observer {

    public Watcher(Watched w) {
        w.addObserver(this);
    }

    @Override
    public void update(Observable ob, Object arg) {
        System.out.println("data has been changed to :" + ((Watched) ob).retreveData());
        //System.out.println("arg:"+arg.toString());
    }

}
