package com.spring.study.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.study.exception.BusinessException;
import com.spring.study.model.User;
import com.spring.study.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private CacheManager ehcacheManager;
    private Cache loginRecordCache;

    @PostConstruct
    public void init() {
        loginRecordCache = ehcacheManager.getCache("sys-userCache");
    }

    @RequestMapping("/saveUser")
    public String saveUser() {
        User user = new User();
        user.setUserId(new Integer(44));
        user.setUserName("wwj");
        user.setPassword("123456");
        Serializable s = userService.saveUser(user);
        System.out.println("Serializable:" + s);
        return "index";
    }

    @RequestMapping("/queryUser")
    public String queryUser() {
//		loginRecordCache.put(new Element("name", "雪莉"));
//		System.out.println("cache用户:" + loginRecordCache.get("name"));
        List<User> allUser = userService.queryUser();
        for (User u : allUser) {
            System.out.println("用户：" + u.getUserName());
        }
        return "index";
    }

    @RequestMapping("/testException")
    public String testException(HttpServletRequest request, Integer id) {
        if (id == null) {
            System.out.println("测试异常...");
            throw new BusinessException("业务异常...");
        }
        return "index";
    }

    @RequestMapping("/testSSL.do")
    public String testSecurity(HttpServletRequest request, String name) {
        System.out.println("测试web.xml安全配置");
        return "index";
    }
}
