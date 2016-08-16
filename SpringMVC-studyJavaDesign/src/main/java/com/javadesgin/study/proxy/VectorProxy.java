package com.javadesgin.study.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.javadesgin.study.model.Person;

/**
 * 实现Vectory代理模式
 * 
 * @author L 2016年4月4日15:55:10
 */
public class VectorProxy implements InvocationHandler {

	private Object proxyObj;

	public VectorProxy(Object vector) {
		this.proxyObj = vector;
	}

	/**
	 * 静态工厂方法
	 * @param obj
	 * @return
	 */
	public static Object factory(Object obj) {
		Class cls = obj.getClass();
		return Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), new VectorProxy(obj));
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("befor calling "+method);
		if(null != args){
			for(int i=0;i<args.length;i++){
				System.out.println("args : "+args[i]);
			}
		}
		Object o = method.invoke(proxyObj, args);
		System.out.println("after calling "+method);
		return o;
	}
	
	public static void main(String[] args) {
		List v = null;
		v = (List) factory(new Vector(10));
		v.add("new ");
		v.add("york");	
		
		/*Person person = null;
		person = (Person) factory(new Person());
		person.setName("sherry");*/
	}

}
