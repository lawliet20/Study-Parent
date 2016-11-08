package com.wwj.dao;

import com.wwj.model.Resource;
import com.wwj.utils.CollectionUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>Resource: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
public class ResourceDaoImpl extends JdbcDaoSupport implements ResourceDao {

    public Resource createResource(final Resource resource) {
        final String sql = "insert into sys_resource(name, type, url, identity,icon, parent_id, parent_ids,weight, available) values(?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"});
                int count = 1;
                psst.setString(count++, resource.getName());
                psst.setString(count++, resource.getType().name());
                psst.setString(count++, resource.getUrl());
                psst.setString(count++, resource.getIdentity());
                psst.setString(count++,resource.getIcon());
                psst.setLong(count++, resource.getParentId());
                psst.setString(count++, resource.getParentIds());
                psst.setInt(count++, resource.getWeight());
                psst.setBoolean(count++, resource.getAvailable());
                return psst;
            }
        }, keyHolder);
        resource.setId(keyHolder.getKey().longValue());
        return resource;
    }

    @Override
    public Resource updateResource(Resource resource) {
        final String sql = "update sys_resource set name=?, type=?, url=?, identity=?,icon=?, parent_id=?, parent_ids=?,weight=? available=? where id=?";
        getJdbcTemplate().update(
                sql,
                resource.getName(), resource.getType().name(), resource.getUrl(), resource.getIdentity(), resource.getParentId(), resource.getParentIds(),resource.getWeight(), resource.getAvailable(), resource.getId());
        return resource;
    }

    public void deleteResource(Long resourceId) {
        Resource resource = findOne(resourceId);
        final String deleteSelfSql = "delete from sys_resource where id=?";
        getJdbcTemplate().update(deleteSelfSql, resourceId);
        final String deleteDescendantsSql = "delete from sys_resource where parent_ids like ?";
        getJdbcTemplate().update(deleteDescendantsSql, resource.makeSelfAsParentIds() + "%");
    }


    @Override
    public Resource findOne(Long resourceId) {
        final String sql = "select id, name, type, url,icon, identity, parent_id, parent_ids,weight, available from sys_resource where id=?";
        List<Resource> resourceList = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Resource.class), resourceId);
        if(CollectionUtil.isEmpty(resourceList)) {
            return null;
        }
        return resourceList.get(0);
    }

    @Override
    public List<Resource> findAll() {
        final String sql = "select id, name, type, url,icon, identity, parent_id, parent_ids,weight, available from sys_resource order by concat(parent_ids, id, weight) asc";
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Resource.class));
    }

}
