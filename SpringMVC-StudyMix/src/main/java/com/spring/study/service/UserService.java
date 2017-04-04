package com.spring.study.service;

import com.spring.study.model.User;

import java.io.Serializable;
import java.util.List;

public interface UserService {

    public void sayHi(String name);

    public Serializable saveUser(User user);

    public List<User> queryUser();
}
