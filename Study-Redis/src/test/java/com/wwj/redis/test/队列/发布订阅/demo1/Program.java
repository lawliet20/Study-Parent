package com.wwj.redis.test.队列.发布订阅.demo1;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Program {

    public static final String CHANNEL_NAME = "commonChannel";

    public static void main(String[] args) throws Exception {

        JedisPoolConfig poolConfig = new JedisPoolConfig();

        JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 7001, 0);

        final Jedis subscriberJedis = jedisPool.getResource();

        final Subscriber subscriber = new Subscriber();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Subscribing to \"commonChannel\". This thread will be blocked.");
                    subscriberJedis.subscribe(subscriber, CHANNEL_NAME);
                    System.out.println("Subscription ended.");
                } catch (Exception e) {
                    //logger.error("Subscribing failed.", e);
                    e.printStackTrace();
                }
            }
        }).start();

        Jedis publisherJedis = jedisPool.getResource();

        new Publisher(publisherJedis, CHANNEL_NAME).start();

        subscriber.unsubscribe();
        jedisPool.returnResource(subscriberJedis);
        jedisPool.returnResource(publisherJedis);
    }
}