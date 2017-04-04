package com.spring.study.dao.impl;

import com.spring.study.dao.UserDao;
import com.spring.study.model.User;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
    @Resource
    private SessionFactory sessionFactory;

    @Override
    public Serializable saveUser(User user) {
        return sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public List<User> queryAllUser() {
        return (List<User>) ((SQLQuery) sessionFactory.getCurrentSession().createSQLQuery("select * from user ")).addEntity(User.class).list();
    }

    @Override
    public List<User> queryUserByName(String name) {
       return (List<User>) ((SQLQuery) sessionFactory.getCurrentSession().createSQLQuery("select * from user where user_name = :name").setParameter("name", name)).addEntity(User.class).list();
    }
}
