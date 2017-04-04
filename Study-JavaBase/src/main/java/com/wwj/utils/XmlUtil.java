package com.wwj.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.wwj.entity.Address;
import com.wwj.entity.Addresses;
import com.wwj.entity.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xml工具类
 * Created by sherry on 2016/11/13.
 */
public class XmlUtil {

    /**
     * 将对象转成xml字符串
     */
    public static String obj2Xml(Object obj) {
        return obj2Xml(obj, null);
    }

    /**
     * 将对象转成xml字符串
     */
    public static String obj2Xml(Object obj,Map<String, Class> map) {
        XStream xstream = createStreamInstance();
        if (CollectionUtil.isNotEmpty(map)) {
            for (Map.Entry<String, Class> entry : map.entrySet()) {
                xstream.alias(entry.getKey(), entry.getValue());
            }
        }
        return xstream.toXML(obj);
    }

    /**
     * 将xml转成对象
     */
    public static Object xml2Obj(String xml) {
        return xml2Obj(xml, null);
    }

    /**
     * 将xml转对象
     * @param xml  带转的xml字符串
     * @param map  xml中节点对应的对象class
     */
    public static Object xml2Obj(String xml, Map<String, Class> map) {
        XStream xstream = createStreamInstance();
        if (CollectionUtil.isNotEmpty(map)) {
            for (Map.Entry<String, Class> entry : map.entrySet()) {
                xstream.alias(entry.getKey(), entry.getValue());
            }
        }
        return xstream.fromXML(xml);
    }

    /**
     * xml转json字符串
     */
    public static String xml2Json(String xml){
        return xml2Json(xml,null);
    }

    /**
     * xml转json字符串
     * @param xml xml字符串
     * @param map xml中节点对象对应的class
     */
    public static String xml2Json(String xml,Map<String, Class> map){
        XStream xStream = createJsonStreamInstance();
        if (CollectionUtil.isNotEmpty(map)) {
            for (Map.Entry<String, Class> entry : map.entrySet()) {
                xStream.alias(entry.getKey(), entry.getValue());
            }
        }
        return xStream.toXML(xml2Obj(xml,map));
    }

    /**
     * 创建普通xstream对象实例
     */
    public static XStream createStreamInstance() {
        XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        return xstream;
    }

    /**
     * 创建json驱动的xstream
     */
    public static XStream createJsonStreamInstance(){
        XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        return xstream;
    }

}
