package com.spring.study.model.pageModel;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 7792729L;
	private int id;
	private String content;
	public String name = "sherry";
	public static String msg = "hi im message!";

	public Message() {

	}

	public Message(int id,String countent) {
		this.id = id;
		this.content = content;
	}
	
	public String sayHi(){
		return "hello world";
	}
	
	public String sayHi(String msg){
		return "hi "+msg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
