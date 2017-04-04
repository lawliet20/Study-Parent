package com.spring.study.service.impl;

import com.spring.study.dao.UserDao;
import com.spring.study.model.User;
import com.spring.study.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public Serializable saveUser(User user) {
        return userDao.saveUser(user);
    }

    @Override
    public void sayHi(String name) {
        System.out.println("my name is " + name);
    }

    @Override
    public List<User> queryUser() {
        return userDao.queryAllUser();
    }

}
