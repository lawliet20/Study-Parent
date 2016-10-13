package com.study.springcore;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * 编程式使用ioc容器
 *
 * @author L
 *         2016年5月8日14:34:47
 */
public class SimpleSpringIoc {

    public static void main(String[] args) {
        simpleUseIoc();
    }

    /**
     * 创建ioc容器步骤
     * 1、创建ioc配置文件的抽象资源，这个抽象资源包含了BeanDefinition的定义信息
     * 2、创建一个BeanFactory，这里使用DefaultListbleBeanFatory
     * 3、创建一个载入BeanDefinition的读入器，这里使用XmlBeanDefinitionReader来载入xml形式的文件BeanDefinition，
     * 通过一个回调配置给BeanFactory
     */
    public static void simpleUseIoc() {
        ClassPathResource res = new ClassPathResource("spring.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(res);
    }

}
