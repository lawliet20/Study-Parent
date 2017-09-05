package com.wwj.simpleIOC;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

/**
 * 传智传客版容器
 */
public class ItcastClassPathXMLApplicationContext {

    private List<BeanDefinition> beanDefines = new ArrayList<BeanDefinition>();
    private Map<String, Object> sigletons = new HashMap<String, Object>();

    public ItcastClassPathXMLApplicationContext(String filename) {
        this.readXML(filename);
        this.instanceBeans();
        this.injectObject();
    }

    /**
     * 为bean对象的属性注入值
     */
    private void injectObject() {
        for (BeanDefinition beanDefinition : beanDefines) {
            Object bean = sigletons.get(beanDefinition.getId());
            if (bean != null) {
                try {
                    PropertyDescriptor[] ps = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
                    for (PropertyDefinition propertyDefinition : beanDefinition.getPropertys()) {
                        for (PropertyDescriptor properdesc : ps) {
                            if (propertyDefinition.getName().equals(properdesc.getName())) {
                                Method setter = properdesc.getWriteMethod();//获取属性的setter方法 ,private
                                if (setter != null) {
                                    Object value = sigletons.get(propertyDefinition.getRef());
                                    setter.setAccessible(true);
                                    setter.invoke(bean, value);//把引用对象注入到属性
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public static void main(String[] args) {
        BeanDefinition bean = new BeanDefinition("1","test");
        try {
            PropertyDescriptor[] ps = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();

            for (PropertyDescriptor properdesc : ps) {
                System.out.println(properdesc.getName());
            }

        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 完成bean的实例化
     */
    private void instanceBeans() {
        for (BeanDefinition beanDefinition : beanDefines) {
            try {
                if (beanDefinition.getClassName() != null && !"".equals(beanDefinition.getClassName().trim()))
                    sigletons.put(beanDefinition.getId(), Class.forName(beanDefinition.getClassName()).newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 读取xml配置文件
     *
     * @param filename
     */
    private void readXML(String filename) {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            URL xmlpath = this.getClass().getClassLoader().getResource(filename);
            document = saxReader.read(xmlpath);
            Map<String, String> nsMap = new HashMap<String, String>();
            nsMap.put("ns", "http://www.springframework.org/schema/beans");//加入命名空间
            XPath xsub = document.createXPath("//ns:beans/ns:bean");//创建beans/bean查询路径
            xsub.setNamespaceURIs(nsMap);//设置命名空间
            List<Element> beans = xsub.selectNodes(document);//获取文档下所有bean节点
            for (Element element : beans) {
                String id = element.attributeValue("id");//获取id属性值
                String clazz = element.attributeValue("class"); //获取class属性值

                BeanDefinition beanDefine = new BeanDefinition(id, clazz);
                XPath propertysub = element.createXPath("ns:property");
                propertysub.setNamespaceURIs(nsMap);//设置命名空间
                List<Element> propertys = propertysub.selectNodes(element);
                for (Element property : propertys) {
                    String propertyName = property.attributeValue("name");
                    String propertyref = property.attributeValue("ref");
                    PropertyDefinition propertyDefinition = new PropertyDefinition(propertyName, propertyref);
                    beanDefine.getPropertys().add(propertyDefinition);
                }
                beanDefines.add(beanDefine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取bean实例
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return this.sigletons.get(beanName);
    }
}
