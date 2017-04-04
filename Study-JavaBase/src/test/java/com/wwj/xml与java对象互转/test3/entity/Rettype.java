package com.wwj.xml与java对象互转.test3.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;


/**
 * Created by sherry on 2016/11/8.
 */
@XStreamAlias("rettype")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"content"})
public class Rettype {

    private String content;
    @XStreamAsAttribute
    private String succval;

    public Rettype(){

    }

    public Rettype(String content,String succval){
        this.content = content;
        this.succval = succval;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSuccval() {
        return succval;
    }

    public void setSuccval(String succval) {
        this.succval = succval;
    }
}
