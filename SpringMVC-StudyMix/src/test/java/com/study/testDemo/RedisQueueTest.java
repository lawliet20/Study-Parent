package com.study.testDemo;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.study.model.pageModel.Message;
import com.spring.study.utils.JedisUtil;
import com.spring.study.utils.ObjectUtil;

@RunWith(SpringJUnit4ClassRunner.class)
public class RedisQueueTest {
	
	public static void main(String[] args) {
		testJavaQeue();
		testJavaQeue2();
	}

	public static void testJavaQeue(){
        Queue<String> queue = new LinkedList<String>();  
        queue.offer("name");
        queue.offer("wwj");
        queue.offer("my name is wwj");
        
//        System.out.println("queue.size is "+queue.size());
//        for(String str:queue){
//        	System.out.println(str);
//        }
	}
	
	public static void testJavaQeue2(){
        Queue<String> queue = new LinkedList<String>();  
//        queue.offer("name");
//        queue.offer("wwj");
//        queue.offer("my name is wwj");
        
        System.out.println("queue.size is "+queue.size());
        for(String str:queue){
        	System.out.println(str);
        }
	}
	
}
