package com.spring.study.dao;

import com.spring.study.model.User;

import java.io.Serializable;
import java.util.List;

public interface UserDao {
    public Serializable saveUser(User user);

    public List<User> queryAllUser();

    public List<User> queryUserByName(String name);
}
