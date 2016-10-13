package com.spring.study.dao;

import java.io.Serializable;
import java.util.List;

import com.spring.study.model.User;

public interface UserDao {
    public Serializable saveUser(User user);

    public List<User> queryAllUser();
}
