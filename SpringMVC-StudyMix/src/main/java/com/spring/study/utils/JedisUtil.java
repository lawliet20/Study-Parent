package com.spring.study.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisUtil {

    // Redis服务器IP
    private static String ADDR = "localhost";

    // Redis的端口号
    private static int PORT = 6379;

    // 访问密码
    private static String AUTH = "tiger";

    // 可用连接实例的最大数目，默认值为8；
    // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;

    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;

    // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;

    private static int TIMEOUT = 10000;

    // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池 高版本"maxActive" -> "maxTotal" and "maxWait" -> "maxWaitMillis"
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            // config.setMaxActive(MAX_ACTIVE);
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            // config.setMaxWait(MAX_WAIT);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            value = jedis.get(key);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 设置Jedis实例
     *
     * @return
     */
    public static void set(String key, String value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void close(Jedis jedis) {
        try {
            returnResource(jedis);
        } catch (Exception e) {
            if (jedis.isConnected()) {
                jedis.quit();
                jedis.disconnect();
            }
        }
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static byte[] get(byte[] key) {

        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }

        return value;
    }

    public static void set(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
    }

    public static void set(byte[] key, byte[] value, int time) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, time);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
    }

    public static void hset(byte[] key, byte[] field, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
    }

    public static void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static String hget(String key, String field) {

        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }

        return value;
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static byte[] hget(byte[] key, byte[] field) {

        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }

        return value;
    }

    public static void hdel(byte[] key, byte[] field) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hdel(key, field);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
    }

    /**
     * 存储REDIS队列 顺序存储
     *
     * @param key
     * @param value
     */
    public static void lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lpush(key, value);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
    }

    /**
     * 存储REDIS队列 顺序存储
     *
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public static void lpush(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lpush(key, value);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            // 返还到连接池
            close(jedis);

        }
    }

    /**
     * 存储REDIS队列 反向存储
     *
     * @param key
     * @param value
     */
    public static void rpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.rpush(key, value);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            // 返还到连接池
            close(jedis);

        }
    }

    /**
     * 存储REDIS队列 反向存储
     *
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public static void rpush(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.rpush(key, value);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
    }

    /**
     * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
     *
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public static void rpoplpush(byte[] key, byte[] destination) {

        Jedis jedis = null;
        try {

            jedis = jedisPool.getResource();
            jedis.rpoplpush(key, destination);

        } catch (Exception e) {

            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {

            // 返还到连接池
            close(jedis);

        }
    }

    /**
     * 获取队列数据
     *
     * @param key
     * @return
     */
    public static String lpop(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.lpop(key);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
        return value;
    }

    /**
     * 获取队列数据
     *
     * @param byte[] key 键名
     * @return
     */
    public static List<byte[]> lpopList(byte[] key) {
        List<byte[]> list = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            list = jedis.lrange(key, 0, -1);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
        return list;
    }

    /**
     * 获取队列数据
     *
     * @param key
     * @return
     */
    public static String rpop(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.rpop(key);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
        return value;
    }

    /**
     * 获取队列数据
     *
     * @param byte[] key 键名
     * @return
     */
    public static byte[] rpop(byte[] key) {
        byte[] bytes = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            bytes = jedis.rpop(key);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
        return bytes;
    }

    public static void hmset(Object key, Map<String, String> hash) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hmset(key.toString(), hash);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            // 返还到连接池
            close(jedis);

        }
    }

    public static void hmset(Object key, Map<String, String> hash, int time) {
        Jedis jedis = null;
        try {

            jedis = jedisPool.getResource();
            jedis.hmset(key.toString(), hash);
            jedis.expire(key.toString(), time);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            // 返还到连接池
            close(jedis);

        }
    }

    public static List<String> hmget(Object key, String... fields) {
        List<String> result = null;
        Jedis jedis = null;
        try {

            jedis = jedisPool.getResource();
            result = jedis.hmget(key.toString(), fields);

        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            // 返还到连接池
            close(jedis);

        }
        return result;
    }

    public static Set<String> hkeys(String key) {
        Set<String> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hkeys(key);

        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            // 返还到连接池
            close(jedis);

        }
        return result;
    }

    public static List<byte[]> lrange(byte[] key, int from, int to) {
        List<byte[]> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lrange(key, from, to);

        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            // 返还到连接池
            close(jedis);

        }
        return result;
    }

    public static Map<byte[], byte[]> hgetAll(byte[] key) {
        Map<byte[], byte[]> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hgetAll(key);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {
            // 返还到连接池
            close(jedis);
        }
        return result;
    }

    public static void del(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
    }

    public static long llen(byte[] key) {

        long len = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.llen(key);
        } catch (Exception e) {
            // 释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            close(jedis);
        }
        return len;
    }
}
