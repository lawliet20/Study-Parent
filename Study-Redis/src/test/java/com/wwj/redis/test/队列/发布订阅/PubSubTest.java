package com.wwj.redis.test.队列.发布订阅;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * redis发布订阅demo
 * Created by sherry on 2016/12/26.
 */
public class PubSubTest {

    /**
     * 开始发布news
     */
    @Test
    public void publishTest() throws InterruptedException {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 7001, 0);

        Jedis jedis = jedisPool.getResource();

        new Thread(){
            @Override
            public void run() {
                try {
                    redisClien1();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                try {
                    redisClien2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        Jedis publisherJedis = jedisPool.getResource();
        Thread.sleep(2000);
        System.out.println("star...");
        publisherJedis.publish("news", "hello every");
        Thread.sleep(2000);
        jedis.publish("news", "你好世界");
    }

    /**
     * 客户端1订阅news频道
     */
    public void redisClien1() throws InterruptedException {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 7001, 0);

        Jedis jedis = jedisPool.getResource();
        System.out.println("im redis-client-1");
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                super.onMessage(channel, message);
                System.out.println("channel:" + channel);
                System.out.println("message:" + message);
            }
        };
        //订阅频道
        jedis.subscribe(jedisPubSub, "news");
    }

    /**
     * 客户端1订阅news频道
     */
    public void redisClien2() throws InterruptedException {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 7001, 0);

        Jedis jedis = jedisPool.getResource();
        System.out.println("im redis-client-2");
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                super.onMessage(channel, message);
                System.out.println("channel:" + channel);
                System.out.println("message:" + message);
            }
        };
        //订阅频道
        jedis.subscribe(jedisPubSub, "news");
    }

}
