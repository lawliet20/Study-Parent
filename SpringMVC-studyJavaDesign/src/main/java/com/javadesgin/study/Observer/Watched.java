package com.javadesgin.study.Observer;

import java.util.Observable;

/**
 * 发布订阅模式（被观察者）
 * @author L
 * 2016年5月2日12:24:32
 * 发布订阅模式demo测试
 */
public class Watched extends Observable {

	private String data = "";
	
	/**
	 * 取值方法
	 */
	public String retreveData(){
		return data;
	}
	
	/**
	 * 改值方法
	 */
	public void changeData(String data){
		if(!this.data.equals(data)){
			this.data = data;
			setChanged();
		}
		notifyObservers();
	}
	
	
	
	
}
