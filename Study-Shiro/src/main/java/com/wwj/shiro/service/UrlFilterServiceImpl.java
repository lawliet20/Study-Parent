package com.wwj.shiro.service;

import com.wwj.dao.UrlFilterDao;
import com.wwj.model.UrlFilter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-25
 * <p>Version: 1.0
 */
@Service
public class UrlFilterServiceImpl implements UrlFilterService {

    @Resource
    private UrlFilterDao urlFilterDao;

    @Resource
    private ShiroFilterChainManager shiroFilerChainManager;

    @Override
    public UrlFilter createUrlFilter(UrlFilter urlFilter) {
        urlFilterDao.createUrlFilter(urlFilter);
        initFilterChain();
        return urlFilter;
    }

    @Override
    public UrlFilter updateUrlFilter(UrlFilter urlFilter) {
        urlFilterDao.updateUrlFilter(urlFilter);
        initFilterChain();
        return urlFilter;
    }

    @Override
    public void deleteUrlFilter(Long urlFilterId) {
        urlFilterDao.deleteUrlFilter(urlFilterId);
        initFilterChain();
    }

    @Override
    public UrlFilter findOne(Long urlFilterId) {
        return urlFilterDao.findOne(urlFilterId);
    }

    @Override
    public List<UrlFilter> findAll() {
        return urlFilterDao.findAll();
    }

    /**
     * 项目启动时，会从数据库读取数据，初始化权限
     */
    @PostConstruct
    public void initFilterChain() {
        shiroFilerChainManager.initFilterChains(findAll());
    }

    public void setUrlFilterDao(UrlFilterDao urlFilterDao) {
        this.urlFilterDao = urlFilterDao;
    }

    public void setShiroFilerChainManager(ShiroFilterChainManager shiroFilerChainManager) {
        this.shiroFilerChainManager = shiroFilerChainManager;
    }
}
