package com.javadesgin.study.观察者模式_发布订阅模式;

/**
 * Created by sherry on 2016/11/20.
 */
public class Client {

    public static void main(String[] args) {
        Watched watched = new Watched();
        Watcher watcher1 = new Watcher(watched);
//        Watcher watcher2 = new Watcher(watched);
//        Watcher watcher3 = new Watcher(watched);
//        Watcher watcher4 = new Watcher(watched);

        watched.changeData("wwj is cool");
        watched.changeData("i love you,wwj");
        watched.changeData("i love you,wwj");
        watched.changeData("i like you ");

    }
}
