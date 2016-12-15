package com.wwj.xml与java对象互转.test3.entity;

/**
 * Created by sherry on 2016/11/8.
 */
public class Event {

    private String name;
    private String objstate;
    private String arg;
    private String function;
    private Rettype rettype;
    //private String rettype;
    //private String succval;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjstate() {
        return objstate;
    }

    public void setObjstate(String objstate) {
        this.objstate = objstate;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

//    public String getRettype() {
//        return rettype;
//    }
//
//    public void setRettype(String rettype) {
//        this.rettype = rettype;
//    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Rettype getRettype() {
        return rettype;
    }

    public void setRettype(Rettype rettype) {
        this.rettype = rettype;
    }

    //    public String getSuccval() {
//        return succval;
//    }
//
//    public void setSuccval(String succval) {
//        this.succval = succval;
//    }
}
