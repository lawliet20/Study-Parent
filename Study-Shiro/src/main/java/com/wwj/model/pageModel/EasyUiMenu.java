/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wwj.model.pageModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 界面是那个使用的菜单对象
 * <p>User: wwj
 * <p>Date: 2016年10月19日11:22:05
 * <p>Version: 1.0
 */
public class EasyUiMenu implements Serializable {
    private Long id;
    private String text;
    private String state = "close";
    private boolean checked = false;
    private String attributes;
    private String iconCls;
    private String url;

    private List<EasyUiMenu> children;

    public EasyUiMenu(Long id, String name, String icon, String url) {
        this.id = id;
        this.text = name;
        this.iconCls = icon;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<EasyUiMenu> getChildren() {
        if (children == null) {
            children = new ArrayList<EasyUiMenu>();
        }
        return children;
    }

    public void setChildren(List<EasyUiMenu> children) {
        this.children = children;
    }

    /**
     * @return
     */
    public boolean isHasChildren() {
        return !getChildren().isEmpty();
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + text + '\'' +
                ", iconCls='" + iconCls + '\'' +
                ", url='" + url + '\'' +
                ", state='" + state + '\'' +
                ", checked='" + checked + '\'' +
                ", attributes='" + attributes + '\'' +
                ", children=" + children +
                '}';
    }
}
