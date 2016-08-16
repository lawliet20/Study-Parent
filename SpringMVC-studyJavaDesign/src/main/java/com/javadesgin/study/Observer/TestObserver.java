package com.javadesgin.study.Observer;

import java.util.Observable;
import java.util.Observer;

public class TestObserver {
	static private Watched watched;
	static private Observer watcher;

	public static void main(String[] args) {
		//创建被观察者对象
		watched = new Watched();
		//创建观察者对象，并将观察者对象登记
		watcher = new Watcher(watched);
		//给被观察者对象的状态赋值四次
		watched.changeData("in c,we create bugs");
		watched.changeData("in java ,we inherit bugs");
		watched.changeData("in java ,we inherit bugs");
		watched.changeData("in visual basic, we visualize bugs");
	}
	
	
}
