package com.dubbo.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {

	public static void main(String[] args) {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring-dubboProvider.xml" });
			context.start();
			System.out.println("Service start...Come on baby!!!!!!!!!");
			synchronized (Provider.class) {
				while (true) {
					try {
						Provider.class.wait();
		            } catch (Throwable e) {
		            }
				}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
