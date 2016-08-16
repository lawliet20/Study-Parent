package com.aop.javaReflection.jdkReflection;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * java反射demo
 * Created by L on 2016/5/26.
 */
public class Main {
    public static void main(String[] args) {
        // 普通创建类的实例
        People p1 = new People();
        System.out.println(p1.getName());
        // 利用反射获取类的实例
        Class clazz = People.class;
        // 常用方式,注意括号中需要放类的全路径名
        // Class clazz = Class.forName("reflection.People");
        // Class clazz = p1.getClass();
        try {
            People p2 = (People) clazz.newInstance();
            People p3 = (People) Class.forName(clazz.getName()).newInstance();
            People p4 = (People) Class.forName("com.aop.javaReflection.jdkReflection.People").newInstance();
            System.out.println(p2.getName());
            System.out.println(p3.getName());
            System.out.println(p4.getName());

            // 获取类中的所有成员变量
            Field[] fields = People.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                //设置权限
                fields[i].setAccessible(true);
                System.out.println("name:" + fields[i].getName());
                System.out.println("字段类型:" + fields[i].getGenericType());
                System.out.println("getType:" + fields[i].getType());
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通過反射机制编写json转bean的小工具
     *
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToBean(JSONObject response, Class<T> clazz) {
        try {
            // 创建类的实例
            Object object = Class.forName(clazz.getName()).newInstance();
            // 获取类中的所有成员变量
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                //设置权限
                fields[i].setAccessible(true);
                // 获取字段的名称
                String fieldName = fields[i].getName();
                // 过滤掉UID
                if (fieldName.endsWith("serialVersionUID")) {
                    continue;
                }
                // 获取字段的类型
                String fieldType = fields[i].getGenericType().toString();
                // 拼接出JavaBean中的set方法 这里有一个坑 后面讲解
                String methodName = "set"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                try {
                    // 判断变量类型
                    if (fieldType.endsWith("class java.lang.String")) {
                        // 获取到set方法
                        Method m = object.getClass().getMethod(methodName,String.class);
                        String value = null;
                        try {
                            // 从JsonObj中取出相应的值
                            value = response.getString(fieldName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            value = "";
                        }
                        if (StringUtils.isEmpty(value)) {
                            value = "";
                        } else if (value.endsWith("null")) {
                            value = "";
                        }
                        // 赋值
                        m.invoke(object, value);
                    } else if (fieldType.endsWith("int")
                            || fieldType.endsWith("class java.lang.Integer")) {
                        // int 类型
                        System.out.println();
                        Method m = object.getClass().getMethod(methodName,
                                int.class);
                        int value = -1;
                        try {
                            value = response.getInteger(fieldName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            value = -1;
                        }
                        m.invoke(object, value);

                    } else if (fieldType.endsWith("boolean")
                            || fieldType
                            .endsWith("fieldType:class java.lang.Boolean")) {
                        // boolean 类型
                        Method m = object.getClass().getMethod(methodName,
                                boolean.class);
                        boolean value = false;
                        try {
                            value = response.getBoolean(fieldName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            value = false;
                        }
                        m.invoke(object, value);
                    } else if (fieldType.endsWith("double")
                            || fieldType
                            .endsWith("fieldType:class java.lang.Double")) {
                        // double 类型
                        Method m = object.getClass().getMethod(methodName,
                                double.class);
                        double value = -1D;
                        try {
                            value = response.getDouble(fieldName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            value = -1D;
                        }
                        m.invoke(object, value);
                    } else if (fieldType.endsWith("char")) {
                        // char类型 JSONObject 没有char
                        Method m = object.getClass().getMethod(methodName,
                                String.class);
                        String value = "";
                        try {
                            value = response.getString(fieldName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            value = "";
                        }
                        m.invoke(object, value);
                    } else if (fieldType.endsWith("float")
                            || fieldType
                            .endsWith("fieldType:class java.lang.Float")) {
                        // float类型
                        Method m = object.getClass().getMethod(methodName,
                                double.class);
                        double value = -1D;
                        try {
                            value = response.getDouble(fieldName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            value = -1D;
                        }
                        m.invoke(object, value);

                    } else if (fieldType.endsWith("short")
                            || fieldType
                            .endsWith("fieldType:class java.lang.Short")) {
                        // short
                        Method m = object.getClass().getMethod(methodName,
                                short.class);
                        int value = -1;
                        try {
                            value = response.getInteger(fieldName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            value = -1;
                        }
                        m.invoke(object, value);
                    } else if (fieldType.endsWith("byte")
                            || fieldType
                            .endsWith("fieldType:class java.lang.Byte")) {
                        Method m = object.getClass().getMethod(methodName,
                                byte.class);
                        int value = -1;
                        try {
                            value = response.getInteger(fieldName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            value = -1;
                        }
                        m.invoke(object, value);
                    } else if (fieldType.endsWith("long")
                            || fieldType
                            .endsWith("fieldType:class java.lang.Long")) {
                        Method m = object.getClass().getMethod(methodName,
                                long.class);
                        Long value = -1L;
                        try {
                            value = response.getLong(fieldName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            value = -1L;
                        }
                        m.invoke(object, value);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            return (T) object;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
