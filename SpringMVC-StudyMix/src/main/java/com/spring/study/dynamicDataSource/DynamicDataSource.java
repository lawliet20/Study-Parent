package com.spring.study.dynamicDataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 动态数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private AtomicInteger counter = new AtomicInteger();

    /**
     * master库 dataSource
     */
    private DataSource master;

    /**
     * slaves
     */
    private List<DataSource> slaves;


    @Override
    protected Object determineCurrentLookupKey() {
        //do nothing
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        //do nothing
    }

    /**
     * 根据标识
     * 获取数据源
     */
    @Override
    protected DataSource determineTargetDataSource() {
        DataSource returnDataSource = null;
        if (DataSourceHolder.isMaster()) {
            returnDataSource = master;
        } else if (DataSourceHolder.isSlave()) {
            int count = counter.incrementAndGet();
            if (count > 1000000) {
                counter.set(0);
            }
            int n = slaves.size();
            int index = count % n;
            returnDataSource = slaves.get(index);
        } else {
            returnDataSource = master;
        }
        System.out.println("当前数据源：" + returnDataSource);
        return returnDataSource;
    }

    public DataSource getMaster() {
        return master;
    }

    public void setMaster(DataSource master) {
        this.master = master;
    }

    public List<DataSource> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<DataSource> slaves) {
        this.slaves = slaves;
    }
}
