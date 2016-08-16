package com.spring.study.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.spring.study.dao.UserDao;
import com.spring.study.model.User;

@Repository("userDao")
public class UserDaoImpl implements UserDao{
	@Resource
	private SessionFactory sessionFactory;
	
	@Override
	public Serializable saveUser(User user){
		return sessionFactory.getCurrentSession().save(user);
	}

	@Override
	public List<User> queryAllUser() {
//		SQLQuery sq = sessionFactory.getCurrentSession().createSQLQuery("select * from user where name!=:name");
//		sq.setParameter("name", "wwj");
//		List<User> list = sq.addEntity(User.class).list();
//		return list;
		return (List<User>) ((SQLQuery) sessionFactory.getCurrentSession().createSQLQuery("select * from user where user_name != :name").setParameter("name", "wwj")).addEntity(User.class).list();
	}
}
