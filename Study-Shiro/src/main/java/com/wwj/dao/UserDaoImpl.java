package com.wwj.dao;

import com.wwj.model.User;
import com.wwj.utils.CastUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    public User createUser(final User user) {
        final String sql = "insert into sys_users(username, password, salt, locked) values(?,?,?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"});
                psst.setString(1, user.getUsername());
                psst.setString(2, user.getPassword());
                psst.setString(3, user.getSalt());
                psst.setBoolean(4, user.getLocked());
                return psst;
            }
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    public void updateUser(User user) {
        String sql = "update sys_users set username=?, password=?, salt=?, locked=? where id=?";
        getJdbcTemplate().update(sql, user.getUsername(), user.getPassword(), user.getSalt(), user.getLocked(), user.getId());
    }

    public void deleteUser(Long userId) {
        String sql = "delete from sys_users where id=?";
        getJdbcTemplate().update(sql, userId);
    }

    @Override
    public void correlationRoles(Long userId, Long... roleIds) {
        if(roleIds == null || roleIds.length == 0) {
            return;
        }
        String sql = "insert into sys_users_roles(user_id, role_id) values(?,?)";
        for(Long roleId : roleIds) {
            if(!exists(userId, roleId)) {
                getJdbcTemplate().update(sql, userId, roleId);
            }
        }
    }

    @Override
    public void uncorrelationRoles(Long userId, Long... roleIds) {
        if(roleIds == null || roleIds.length == 0) {
            return;
        }
        String sql = "delete from sys_users_roles where user_id=? and role_id=?";
        for(Long roleId : roleIds) {
            if(exists(userId, roleId)) {
                getJdbcTemplate().update(sql, userId, roleId);
            }
        }
    }

    private boolean exists(Long userId, Long roleId) {
        String sql = "select count(1) from sys_users_roles where user_id=? and role_id=?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, userId, roleId) != 0;
    }


    @Override
    public User findOne(Long userId) {
        String sql = "select id, username, password, salt, locked from sys_users where id=?";
        List<User> userList = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(User.class), userId);
        if(userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }

    @Override
    public User findByUsername(String username) {
        String sql = "select id, username, password, salt, locked from sys_users where username=?";
        List<User> userList = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(User.class), username);
        if(userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }

    @Override
    public Set<String> findRoles(String username) {
        String sql = "select role from sys_users u, sys_roles r,sys_users_roles ur where u.username=? and u.id=ur.user_id and r.id=ur.role_id";
        return new HashSet(getJdbcTemplate().queryForList(sql, String.class, username));
    }

    @Override
    public Set<String> findPermissions(String username) {
        String sql = "select permission from sys_users u, sys_roles r, sys_permissions p, sys_users_roles ur, sys_roles_permissions rp where u.username=? and u.id=ur.user_id and r.id=ur.role_id and r.id=rp.role_id and p.id=rp.permission_id";
        return new HashSet(getJdbcTemplate().queryForList(sql, String.class, username));
    }

    @Override
    public Set<String> findPermissions(Set<String> roles){
        String sql = "select permission from sys_permissions p,sys_roles r,sys_roles_permissions rp " +
                "where r.id in(:roleIds) and rp.role_id=r.id and rp.permission_id=p.id";
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("roleIds",new ArrayList<>(roles));
        return new HashSet(namedParameterJdbcTemplate.queryForList(sql,parameters,String.class));
    }

}
