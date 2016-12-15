package com.wwj.xml与java对象互转.test3.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

/**
 * Created by sherry on 2016/11/8.
 */
@XStreamAlias("state")
public class State {

    @XStreamAsAttribute
    @XStreamAlias("start")
    private String start;
    private String name;
    private List<Event> events;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
