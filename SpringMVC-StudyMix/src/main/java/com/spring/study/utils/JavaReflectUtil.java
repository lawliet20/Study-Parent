package com.spring.study.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.spring.study.model.pageModel.Message;

/**
 * java反射机制demo
 *
 * @author L
 *         2016年4月10日21:51:39
 */
public class JavaReflectUtil {

    public static void main(String[] args) throws Exception {
        Message msg = new Message(1, "sherry");
        JavaReflectUtil jrf = new JavaReflectUtil();
        String publicPro = (String) jrf.getProperty(msg, "name");
        String staticPro = (String) jrf.getStaticProperty(Message.class.getName().toString(), "msg");
        String invokeRes1 = (String) jrf.invokeMethod(msg, "sayHi", new String[]{});
        String invokeRes2 = (String) jrf.invokeMethod(msg, "sayHi", new String[]{"sherry"});
        System.out.println(publicPro);
        System.out.println(staticPro);
        System.out.println(invokeRes1);
        System.out.println(invokeRes2);

    }

    // 得到某个对象的属性
    public static Object getProperty(Object owner, String fieldName) throws Exception {
        Class ownerClass = owner.getClass();
        Field field = ownerClass.getField(fieldName);
        Object property = field.get(owner);
        return property;
    }

    // 得到某个类的静态属性
    public static Object getStaticProperty(String className, String fieldName) throws Exception {
        Class ownerClass = Class.forName(className);
        Field field = ownerClass.getField(fieldName);
        Object property = field.get(ownerClass);
        return property;
    }

    // 执行某对象的方法
    public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
        Class ownerClass = owner.getClass();
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(owner, args);
    }

    // 执行某个类的静态方法
    public static Object invokeStaticMethod(String className, String methodName, Object[] args) throws Exception {
        Class ownerClass = Class.forName(className);
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(null, args);
    }

    // 新建实例
    public static Object newInstance(String className, Object[] args) throws Exception {
        Class newoneClass = Class.forName(className);
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Constructor cons = newoneClass.getConstructor(argsClass);
        return cons.newInstance(args);
    }

    // 判断是否为某个类的实例
    public static boolean isInstance(Object obj, Class cls) {
        return cls.isInstance(obj);
    }

    // 得到数组中的某个元素
    public static Object getByArray(Object array, int index) {
        return Array.get(array, index);
    }
}
