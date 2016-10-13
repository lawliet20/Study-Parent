package com.spring.study.service;

/**
 * 异常服务测试
 *
 * @author L 2016年2月29日23:07:25
 */
public interface ExceptionService {
    public void exception(Integer id) throws Exception;

    public void dao(Integer id) throws Exception;
}
