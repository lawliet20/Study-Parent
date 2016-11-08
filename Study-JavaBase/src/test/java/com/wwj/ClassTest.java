package com.wwj;

import com.wwj.annotation.ClazzAnnotation;
import com.wwj.enums.Enum;
import com.wwj.model.Interface1;
import com.wwj.model.Interface2;
import com.wwj.model.People;
import com.wwj.model.Teacher;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class类方法测试
 * Created by sherry on 2016/10/24.
 */
public class ClassTest {
    private static Class peopleClazz = People.class;
    private static Class teacherClazz = Teacher.class;
    private static People people = new People();
    private static Teacher teacher = new Teacher();

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //asSubjectClass();
        //cast();
        //forName();
        //getAnnotation();
        //getAnnotations();
        //getClasses();
        getDeclaredClasses();
        //getConstructor();
        //getDeclaredField();
        //getField();
        //getDeclaredFields();
        //getDeclaredMethod();
        //getName();
        //getPackage();
        //getGenericInterfaces();
        //getInterfaces();
        //getSuperclass();
        //getUrl();
        //getResourceAsStream();
        //isAnnotation();
        //isAnnotationPresent();
        //isArray();
        //isEnum();
        //isAssignableFrom();
        //isInstance();
        //isInterface();
        //isMemberClass();
    }

    /**
     * 父类作为参数,将父类转化为子类
     */
    public static void asSubjectClass() {
        Class<? extends People> clazz = teacherClazz.asSubclass(peopleClazz);
        System.out.println(clazz.getCanonicalName());
    }

    /**
     * 方法对象转换通过此Class对象表示类或接口
     */
    public static void cast() {
        People people1 = (People) peopleClazz.cast(teacher);
        System.out.println(people1.getClass());
    }

    /**
     * 此方法返回与给定字符串名的类或接口的Class对象
     */
    public static void forName() throws ClassNotFoundException {
        Class<People> people = (Class<People>) Class.forName("com.wwj.model.People");
        System.out.println(people.getCanonicalName());
    }

    /**
     * 获取类指定的注解
     */
    public static void getAnnotation() {
        System.out.println(peopleClazz.getAnnotation(ClazzAnnotation.class));
    }

    /**
     * 获取类上所有的注解
     */
    public static void getAnnotations() {
        Annotation[] arr = peopleClazz.getAnnotations();
        for (Annotation ano : arr) {
            System.out.println(ano);
        }
    }

    /**
     * 返回一个包含某些 Class 对象的数组，这些对象表示属于此 Class 对象所表示的类的成员的所有公共类和接口。
     */
    public static void getClasses() {
        Class<People>[] classes = peopleClazz.getClasses();
        for (Class clazz : classes) {
            System.out.println("###:" + clazz.getName());
        }
    }

    /**
     * 返回 Class 对象的一个数组，这些对象反映声明为此 Class 对象所表示的类的成员的所有类和接口
     */
    public static void getDeclaredClasses() {
        Class<People>[] classes = peopleClazz.getDeclaredClasses();
        for (Class clazz : classes) {
            System.out.println("###:" + clazz.getName());
        }
    }

    /**
     * 获取class的公共的构造函数，getConstructor无法获取私有构造函数
     */
    public static void getConstructor() {
        try {
            Constructor<People> constructor1 = peopleClazz.getConstructor();
            Constructor<People> constructor2 = peopleClazz.getConstructor(String.class, int.class, char.class);

            constructor1.setAccessible(true);
            constructor2.setAccessible(true);

            System.out.println(constructor1.newInstance());
            System.out.println(constructor2.newInstance("wwj", 0, '1'));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取class所有的公共的构造函数，getConstructor无法获取私有构造函数
     */
    public static void getConstructors() {
        Constructor<People>[] arr = peopleClazz.getConstructors();
        for (Constructor constructor : arr) {
            constructor.setAccessible(true);
            System.out.println(constructor.getName());
        }
    }

    /**
     * 获取class的声明的构造函数，包括私有构造函数
     */
    public static void getDeclaredConstructor() {
        //私有构造方法无法通过getConstructor获取
        try {
            Constructor<People> constructor3 = peopleClazz.getDeclaredConstructor(String.class);
            constructor3.setAccessible(true);
            System.out.println(constructor3.newInstance("wwj"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取class的所有声明的构造函数，包括私有构造函数
     */
    public static void getDeclaredConstructors() {
        try {
            Constructor<People>[] arr = peopleClazz.getDeclaredConstructors();
            for (Constructor<People> constructor : arr) {
                constructor.setAccessible(true);
                System.out.println(constructor.newInstance("wwj"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取public声明的字段
     */
    public static void getField() {
        try {
            Field desc = peopleClazz.getField("desc");
            System.out.println(desc.getName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取声明的字段,包括共有与私有的
     */
    public static void getDeclaredField() {
        try {
            Field name = peopleClazz.getDeclaredField("name");
            Field desc = peopleClazz.getDeclaredField("desc");
            System.out.println(name.getName());
            System.out.println(desc.getName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取声明的字段,包括共有与私有的
     */
    public static void getDeclaredFields() {
        try {
            Field[] arr = peopleClazz.getDeclaredFields();
            for (Field field : arr) {
                System.out.println(field.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取class对象所有的方法，包括私有的和共有的
     */
    public static void getDeclaredMethod() {
        try {
            Method sayHi = peopleClazz.getDeclaredMethod("sayHi", String.class);
            //私有方法执行需要设置为true
            sayHi.setAccessible(true);
            sayHi.invoke(people, "lili");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取类中所有的方法,包括私有的方法
     */
    public static void getDeclaredMethods() {
        Method[] methods = peopleClazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
    }

    /**
     * 返回本类直接实现的接口.不包含泛型参数信息
     */
    public static void getInterfaces() {
        Class[] arr = teacherClazz.getInterfaces();
        for (Class clazz : arr) {
            System.out.println(clazz.getCanonicalName());
        }
    }

    /**
     * 返回表示当前对象所表示的类或接口直接实现的接口类型
     */
    public static void getGenericInterfaces() {
        Type[] arr = teacherClazz.getGenericInterfaces();
        for (Type type : arr) {
            //System.out.println(type.getClass());
            System.out.println(type.equals(Interface2.class));
        }
    }

    /**
     * 获取class的直接父类type
     */
    public static void getGenericSuperclass() {
        Type type = teacherClazz.getGenericSuperclass();
        Type type2 = peopleClazz.getGenericSuperclass();
        System.out.println(type.getClass().getCanonicalName());
        System.out.println(type2.getClass().getCanonicalName());
    }

    /**
     * 获取class的直接父类
     */
    public static void getSuperclass() {
        Class clazz = teacherClazz.getSuperclass();
        Class clazz2 = peopleClazz.getSuperclass();
        System.out.println(clazz.getCanonicalName());
        System.out.println(clazz2.getCanonicalName());
    }

    /**
     * 获取class的类名
     */
    public static void getName() {
        //获取类名
        System.out.println(peopleClazz.getName());
        //获取标准的类名（包括包路径）
        System.out.println(peopleClazz.getCanonicalName());
        //获取类名（不包括包路径）
        System.out.println(peopleClazz.getSimpleName());
    }

    /**
     * 获取包信息
     */
    public static void getPackage() {
        System.out.println(peopleClazz.getPackage().getName());
    }

    /**
     * 获取指定路径的资源
     */
    public static void getUrl() {
        URL url = peopleClazz.getResource("/config.properties");
        try {
            InputStream is = url.openStream();
            System.out.println(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(url.getPath());
    }

    /**
     * 获取文件的输入流
     */
    public static void getResourceAsStream() {
        InputStream is = peopleClazz.getResourceAsStream("/config.properties");
        System.out.println(is);
    }

    /**
     * 判断当前类是否是注解类
     */
    public static void isAnnotation(){
        System.out.println(ClazzAnnotation.class.isAnnotation());
        System.out.println(peopleClazz.isAnnotation());
    }

    /**
     * 判断当前类是否是标注了指定的注解
     */
    public static void isAnnotationPresent(){
        System.out.println(peopleClazz.isAnnotationPresent(ClazzAnnotation.class));
        System.out.println(peopleClazz.isAnnotationPresent(Field.class));
    }

    /**
     * 判断当前类是否是数组类
     */
    public static void isArray(){
        List list = new ArrayList<String>();
        String[] arr = new String[4];
        System.out.println(list.getClass().isArray());
        System.out.println(arr.getClass().isArray());
        System.out.println(peopleClazz.isArray());
    }

    /**
     * 判断是否是枚举类型
     */
    public static void isEnum(){
        System.out.println(peopleClazz.isEnum());
        System.out.println(Enum.class.isEnum());
    }

    /**
     *判断一个类Class1和另一个类Class2是否相同或是另一个类的超类或接口
     * instanceof 针对实例
     * isAssignableFrom针对class对象
     */
    public static void isAssignableFrom(){
        System.out.println(peopleClazz.isAssignableFrom(teacherClazz));
        System.out.println(Interface1.class.isAssignableFrom(teacherClazz));
        String str = "";
        System.out.println(str instanceof String);
    }

    /**
     * A类是不是B对象的基类或者接口
     * 参数是一个对象实例
     */
    public static void isInstance(){
        System.out.println(peopleClazz.isInstance(teacher));
        System.out.println(Interface1.class.isInstance(people));
    }

    /**
     * 判断当前类是否是接口类型
     */
    public static void isInterface(){
        System.out.println(Interface1.class.isInterface());
        System.out.println(peopleClazz.isInterface());
    }

    /**
     * 此方法返回true当且仅当基础类的成员类
     */
    public static void isMemberClass(){
        System.out.println(teacherClazz.isMemberClass());
    }

    /**
     * 判断是否是基本数据类型：
     * 它们代表即boolean, byte, char, short, int, long, float, 和double 等原始类型
     */
    public static void isPrimitive(){
        Class baseClass = boolean.class;
        System.out.println(baseClass.isPrimitive());
    }

    /**
     * 实例化class对象
     */
    public static void newInstance(){
        try {
            People p1 = (People) peopleClazz.newInstance();
            System.out.println(p1.getName());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
