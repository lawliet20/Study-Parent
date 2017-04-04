package com.wwj.redis.test.队列.生产消费;

import com.wwj.redis.test.RedisClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * redis生产者消费者队列
 * Created by sherry on 2016/12/28.
 */
public class ProductConsumer {

    //jedis主要是用于redis单机环境的客户端
    private Jedis jedis;
    private ShardedJedis shardedJedis;
    private RedisClient redisClient;

    @Before
    public void connectRedis() {
        //连接redis服务器
        redisClient = new RedisClient();
        jedis = redisClient.getJedis();
        shardedJedis = redisClient.getShardedJedis();
    }

    @After
    public void flushRes(){
        redisClient.flushAllRes();
    }

    /**
     * redis先进先出队列
     */
    @Test
    public void test1(){
        shardedJedis.del("num");
        shardedJedis.del("age");

        shardedJedis.lpush("num","1");
        shardedJedis.lpush("num","2");
        shardedJedis.lpush("num","3");
        shardedJedis.lpush("num","4");

        for(int i=0;i<4;i++){
//            System.out.println("取出队列中值："+shardedJedis.lpop("num"));
            System.out.println("取出队列中值："+shardedJedis.rpop("num"));
        }

        System.out.println("==========================");
        shardedJedis.rpush("age", "1");
        shardedJedis.rpush("age", "2");
        shardedJedis.rpush("age", "3");
        shardedJedis.rpush("age", "4");

        for(int i=0;i<4;i++){
            System.out.println("取出队列中值："+shardedJedis.lpop("age"));
//            System.out.println("取出队列中值："+shardedJedis.rpop("age"));
        }

    }
}
