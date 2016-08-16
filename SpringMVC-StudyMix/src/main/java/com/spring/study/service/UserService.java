package com.spring.study.service;

import java.io.Serializable;
import java.util.List;

import com.spring.study.model.User;

public interface UserService {

	public void sayHi(String name);
	
	public Serializable saveUser(User user);
	
	public List<User> queryUser();
}
