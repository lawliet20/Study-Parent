package com.wwj.redis.test.队列.发布订阅.demo1;

import redis.clients.jedis.JedisPubSub;

public class Subscriber extends JedisPubSub {


    @Override
    public void onMessage(String channel, String message) {
        //logger.info("Message received. Channel: {}, Msg: {}", channel, message);
        System.out.println("message:"+message);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }
}