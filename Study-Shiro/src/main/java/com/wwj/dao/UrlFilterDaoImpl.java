package com.wwj.dao;


import com.wwj.model.UrlFilter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

/**
 * Created by sherry on 16/10/13.
 */
public class UrlFilterDaoImpl extends JdbcDaoSupport implements UrlFilterDao {

    @Override
    public UrlFilter createUrlFilter(UrlFilter urlFilter) {
        return null;
    }

    @Override
    public UrlFilter updateUrlFilter(UrlFilter urlFilter) {
        String sql = "update sys_url_filter set " +
                "name='"+urlFilter.getName()+"'"+
                ",url='"+urlFilter.getUrl()+"'"+
                ",roles='"+urlFilter.getRoles()+"'"+
                ",permissions='"+urlFilter.getPermissions()+"'"+
                " where id = "+urlFilter.getId();
        getJdbcTemplate().update(sql);
        return urlFilter;
    }

    @Override
    public void deleteUrlFilter(Long urlFilterId) {

    }

    @Override
    public UrlFilter findOne(Long urlFilterId) {
        UrlFilter urlFilter = new UrlFilter();
        urlFilter.setId(1l);
        urlFilter.setName("菜单1");
        urlFilter.setPermissions("user:del");
        urlFilter.setRoles("admin");
        urlFilter.setUrl("/test");
        return urlFilter;
    }

    @Override
    public List<UrlFilter> findAll() {
        String sql = "select id, name, permissions, roles, url from sys_url_filter";
        List<UrlFilter> urlFilterList = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(UrlFilter.class));
        return urlFilterList;
    }
}
