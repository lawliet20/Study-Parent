package com.javadesgin.study.合成模式.文件系统;

/**
 * 抽象文件角色
 * Created by sherry on 2016/11/15.
 */
public interface IFile {

    /**
     * 获取当地文件名称
     */
    String getName();

    /**
     * 获取当前实例
     */
    IFile getComposite();

    /**
     * 简单商业逻辑
     */
    void simpleOperation();

    /**
     * 获取深度
     */
    int getDeep();

    /**
     * 设置深度
     */
    void setDeep(int deep);
}
