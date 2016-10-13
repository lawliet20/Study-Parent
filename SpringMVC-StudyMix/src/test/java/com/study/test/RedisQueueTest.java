package com.study.test;

import com.spring.study.model.pageModel.Message;
import com.spring.study.utils.JedisUtil;
import com.spring.study.utils.ObjectUtil;

public class RedisQueueTest {

    public static byte[] redisKey = "key".getBytes();

    static {
        try {
            init();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            pop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pop() throws Exception {
        byte[] bytes = JedisUtil.rpop(redisKey);
        Message msg = (Message) ObjectUtil.bytesToObject(bytes);
        if (msg != null) {
            System.out.println(msg.getId() + "   " + msg.getContent());
        }
    }

    private static void init() throws Exception {
        System.out.println("init...");
        Message msg1 = new Message(1, "内容1");
        JedisUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg1));
        Message msg2 = new Message(2, "内容2");
        JedisUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg2));
        Message msg3 = new Message(3, "内容3");
        JedisUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg3));
    }
}
