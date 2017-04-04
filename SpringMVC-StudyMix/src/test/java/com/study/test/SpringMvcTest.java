package com.study.test;

import com.dubbo.model.PageUser;
import com.dubbo.service.DemoService;
import com.spring.study.model.User;
import com.spring.study.service.ExceptionService;
import com.spring.study.service.UserService;
import com.spring.study.utils.JedisUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
// 类似于extend SpringJUnit4ClassRunner
@ContextConfiguration(locations = {"classpath:spring.xml", "classpath:spring-hibernate.xml"})
public class SpringMvcTest {
    @Resource
    private UserService userService;
    @Resource
    private ExceptionService exceptionService;
    @Resource
    private DemoService demoService;
    // spring分装的redis
//	@Resource
//	private RedisTemplate<String, String> redis;
//	// 原生redis的java客户端
//	private Jedis jedis = JedisUtil.getJedis();
    @Resource
    private CacheManager ehcacheManager;
    private Cache ehCache;

    @PostConstruct
    public void init() {
        ehCache = (Cache) ehcacheManager.getCache("sys-userCache");
    }


    @Test
    public void userTest1() {
        userService.sayHi("雪莉");
        List<User> allUser = userService.queryUser();
        for (User u : allUser) {
            System.out.println("用户：" + u.getUserName());
        }
    }

    @Test
    public void userTest2() {
        User user = new User();
        user.setUserId(new Integer(44));
        user.setUserName("wwj");
        user.setPassword("123456");
        userService.saveUser(user);
    }

    @Test
    public void testDubbo() {
        String hello = demoService.sayHello("tom");
        System.out.println(hello);
        List<PageUser> list = demoService.getUsers();
        /*if (list != null && list.size() > 0) {
			for (PageUser u : list) {
				System.out.println(JSON.toJSONString(u));
			}
		}*/
    }

    @Test
    public void testEhCache() {
        // 新增
        ehCache.put(new Element("userName", "雪莉"));
        System.out.println("userName:" + ehCache.get("userName"));
        // 更新（put或replace）
        ehCache.replace(new Element("userName", "sherry"));
        System.out.println("userName:" + ehCache.get("userName"));
        // 删除
        ehCache.remove("userName");
        System.out.println("userName:" + ehCache.get("userName"));
    }

    @Test
    public void testException() throws Exception {
        System.out.println("exception begin...");
        exceptionService.exception(new Integer(11));
        System.out.println("exception end...");
    }

    @Test
    public void testSetRedis() {
        // 设置属性保存10秒
		/*
		 * redis.opsForValue().set("name", "wwj", 10, TimeUnit.MINUTES); Set<String> keys = redis.keys("*"); Iterator<String> it = keys.iterator(); while (it.hasNext()) { System.out.println("redis中当前的key:" + it); }
		 */
    }

    @Test
    public void testGetRedis() {
        // 读取redis属性
        // System.out.println("redis取值name：" + redis.opsForValue().get("name"));

    }

    @Test
    public void testJedis() {
        // 参考网址：http://www.cnblogs.com/edisonfeng/p/3571870.html
        // set the data in redis string
		/*
		 * jedis.set("tutorial-name", "Redis tutorial"); // Get the stored data and print it System.out.println("Stored string in redis:: " + jedis.get("tutorial-name")); jedis.set("name", "wwj"); System.out.println("Stored string in redis:: " + JedisUtil.get("name")); System.out.println("======================String_1=========================="); // 清空数据 System.out.println("清空库中所有数据：" + jedis.flushDB());
		 */
    }

    /**
     * redis的队列生产者: 只允许在一端插入 新元素只能在队列的尾部 FIFO：先进先出原则 redis中lpush（rpop）或rpush(lpop)可以满足要求
     */
    @Test
    public void testRedisQueueClient() throws Exception {
        // lpush方式插入数据
        // jedis.lpush("name", "sherry");
        // jedis.lpush("name", "wwj");
        JedisUtil.lpush("name", "sherry");
        JedisUtil.lpush("name", "wwj");
        // rpush方式插入数据
        // jedis.rpush("name", "love");
        // jedis.rpush("name", "mylove");
        JedisUtil.rpush("name", "love");
        JedisUtil.rpush("name", "mylove");
    }

    @Test
    public void testRedisQueueConsumer() {
        boolean hasNextRpop = true;
        boolean hasNextLpop = true;
        while (hasNextRpop) {
            // String name = jedis.rpop("name");
            String name = JedisUtil.rpop("name");
            if (!StringUtils.isEmpty(name)) {
                System.out.println("name=" + name);
            } else {
                hasNextRpop = false;
            }
        }

        while (hasNextLpop) {
            // String name = jedis.lpop("name");
            String name = JedisUtil.lpop("name");
            if (!StringUtils.isEmpty(name)) {
                System.out.println("name=" + name);
            } else {
                hasNextLpop = false;
            }
        }
    }

}
