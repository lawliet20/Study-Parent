package com.wwj.redis.test;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 2016/12/20.
 */
public class RedisClient {

    private Jedis jedis;//客户端连接
    private JedisPool jedisPool;//连接池
    private ShardedJedis shardedJedis;//Sharded客户端连接
    private ShardedJedisPool shardedJedisPool;//Sharded连接池

    public static void main(String[] args) {
        RedisClient client = new RedisClient();
        client.show();
    }

    public RedisClient() {
        initialPool();
        initialShardedPool();
        shardedJedis = shardedJedisPool.getResource();
        jedis = jedisPool.getResource();
    }

    /**
     * 初始化非切片池
     * 具体配置参考：http://www.2cto.com/database/201311/254449.html
     */
    private void initialPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);//最大分配的对象数
        config.setMaxIdle(5);//最大能够保持idel状态的对象数
        config.setMaxWaitMillis(1000l);//当池内没有返回对象时，最大等待时间
        config.setTestOnReturn(false);//当调用return Object方法时，是否进行有效性检查
        config.setTestOnBorrow(false);//当调用borrow Object方法时，是否进行有效性检查

        jedisPool = new JedisPool(config, "127.0.0.1", 6379);
    }

    /**
     * 初始化切片池
     */
    private void initialShardedPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000l);
        config.setTestOnBorrow(false);
        // slave链接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379, "master"));

        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
    }

    public void show() {
        /*// key检测
        testKey();
        // string检测
        testString();
        // list检测
        testList();
        // set检测
        testSet();
        // sortedSet检测
        testSortedSet();*/
        // hash检测
        testHash();
//        shardedJedis.close();
    }

    private void testKey() {
        System.out.println("=============key==========================");
        // 清空数据
        System.out.println(jedis.flushDB());
        System.out.println(jedis.echo("foo"));
        // 判断key否存在
        System.out.println(shardedJedis.exists("foo"));
        shardedJedis.set("key", "values");
        System.out.println(shardedJedis.exists("key"));
    }

    private void testString() {
        System.out.println("=============String==========================");
        // 清空数据
        System.out.println(jedis.flushDB());
        // 存储数据
        shardedJedis.set("foo", "bar");
        System.out.println(shardedJedis.get("foo"));
        // 若key不存在，则存储
        shardedJedis.setnx("foo", "foo not exits");
        System.out.println(shardedJedis.get("foo"));
        // 覆盖数据
        shardedJedis.set("foo", "foo update");
        System.out.println(shardedJedis.get("foo"));
        // 追加数据
        shardedJedis.append("foo", " hello, world");
        System.out.println(shardedJedis.get("foo"));
        // 设置key的有效期，并存储数据
        shardedJedis.setex("foo", 2, "foo not exits");
        System.out.println(shardedJedis.get("foo"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        System.out.println(shardedJedis.get("foo"));
        // 获取并更改数据
        shardedJedis.set("foo", "foo update");
        System.out.println(shardedJedis.getSet("foo", "foo modify"));
        // 截取value的值
        System.out.println(shardedJedis.getrange("foo", 1, 3));
        System.out.println(jedis.mset("mset1", "mvalue1", "mset2", "mvalue2",
                "mset3", "mvalue3", "mset4", "mvalue4"));
        System.out.println(jedis.mget("mset1", "mset2", "mset3", "mset4"));
        System.out.println(jedis.del(new String[]{"foo", "foo1", "foo3"}));
    }

    private void testList() {
        System.out.println("=============list==========================");
        // 清空数据
        System.out.println(jedis.flushDB());
        // 添加数据
        shardedJedis.lpush("lists", "vector");
        shardedJedis.lpush("lists", "ArrayList");
        shardedJedis.lpush("lists", "LinkedList");
        // 数组长度
        System.out.println(shardedJedis.llen("lists"));
        // 排序
        //      System.out.println(shardedJedis.sort("lists"));
        // 字串
        System.out.println(shardedJedis.lrange("lists", 0, 3));
        // 修改列表中单个值
        shardedJedis.lset("lists", 0, "hello list!");
        // 获取列表指定下标的值
        System.out.println(shardedJedis.lindex("lists", 1));
        // 删除列表指定下标的值
        System.out.println(shardedJedis.lrem("lists", 1, "vector"));
        // 删除区间以外的数据
        System.out.println(shardedJedis.ltrim("lists", 0, 1));
        // 列表出栈
        System.out.println(shardedJedis.lpop("lists"));
        // 整个列表值
        System.out.println(shardedJedis.lrange("lists", 0, -1));
    }

    private void testSet() {
        System.out.println("=============set==========================");
        // 清空数据
        System.out.println(jedis.flushDB());
        // 添加数据
        shardedJedis.sadd("sets", "HashSet");
        shardedJedis.sadd("sets", "SortedSet");
        shardedJedis.sadd("sets", "TreeSet");
        // 判断value是否在列表中
        System.out.println(shardedJedis.sismember("sets", "TreeSet"));
        // 整个列表值
        System.out.println(shardedJedis.smembers("sets"));
        // 删除指定元素
        System.out.println(shardedJedis.srem("sets", "SortedSet"));
        // 出栈
        System.out.println(shardedJedis.spop("sets"));
        System.out.println(shardedJedis.smembers("sets"));

        shardedJedis.sadd("sets1", "HashSet1");
        shardedJedis.sadd("sets1", "SortedSet1");
        shardedJedis.sadd("sets1", "TreeSet");
        shardedJedis.sadd("sets2", "HashSet2");
        shardedJedis.sadd("sets2", "SortedSet1");
        shardedJedis.sadd("sets2", "TreeSet1");
        // 交集
        System.out.println(jedis.sinter("sets1", "sets2"));
        // 并集
        System.out.println(jedis.sunion("sets1", "sets2"));
        // 差集
        System.out.println(jedis.sdiff("sets1", "sets2"));
    }

    private void testSortedSet() {
        System.out.println("=============zset==========================");
        // 清空数据
        System.out.println(jedis.flushDB());
        // 添加数据
        shardedJedis.zadd("zset", 10.1, "hello");
        shardedJedis.zadd("zset", 10.0, ":");
        shardedJedis.zadd("zset", 9.0, "zset");
        shardedJedis.zadd("zset", 11.0, "zset!");
        // 元素个数
        System.out.println(shardedJedis.zcard("zset"));
        // 元素下标
        System.out.println(shardedJedis.zscore("zset", "zset"));
        // 集合子集
        System.out.println(shardedJedis.zrange("zset", 0, -1));
        // 删除元素
        System.out.println(shardedJedis.zrem("zset", "zset!"));
        System.out.println(shardedJedis.zcount("zset", 9.5, 10.5));
        // 整个集合值
        System.out.println(shardedJedis.zrange("zset", 0, -1));
    }

    private void testHash() {
        System.out.println("=============hash==========================");
        // 清空数据
        System.out.println(jedis.flushDB());
        // 添加数据
        shardedJedis.hset("hashs", "entryKey", "entryValue");
        shardedJedis.hset("hashs", "entryKey1", "entryValue1");
        shardedJedis.hset("hashs", "entryKey2", "entryValue2");
        // 判断某个值是否存在
        System.out.println(shardedJedis.hexists("hashs", "entryKey"));
        // 获取指定的值
        System.out.println(shardedJedis.hget("hashs", "entryKey"));
        // 批量获取指定的值
        System.out.println(shardedJedis.hmget("hashs", "entryKey", "entryKey1"));
        // 删除指定的值
        System.out.println(shardedJedis.hdel("hashs", "entryKey"));
//        System.out.println(shardedJedis.del("hashs"));
        // 为key中的域 field 的值加上增量 increment
        System.out.println(shardedJedis.hincrBy("hashs", "entryKey", 123l));
        // 获取所有的keys
        System.out.println(shardedJedis.hkeys("hashs"));
        // 获取所有的values
        System.out.println(shardedJedis.hvals("hashs"));

    }


    /**
     * 释放jedis资源
     */
    public void flushJedisRes() {
        if (null != jedis) {
            jedis.close();
        }
    }

    /**
     * 释放sharedJedis资源
     */
    public void flushSharedJedisRes() {
        if (null != shardedJedis) {
            shardedJedis.close();
        }
    }

    /**
     * 释放所有的资源
     */
    public void flushAllRes() {
        flushJedisRes();
        flushSharedJedisRes();
    }

    public Jedis getJedis() {
        return jedis;
    }

    public ShardedJedis getShardedJedis() {
        return shardedJedis;
    }

}
