package com.wwj.xml与java对象互转.test2.entity;

import java.util.List;

public class ListBean {

private String name;
private List<Object> list;

public List<Object> getList() {
return list;
}

public void setList(List<Object> list) {
this.list = list;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

}