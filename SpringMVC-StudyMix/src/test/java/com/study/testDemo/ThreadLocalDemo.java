package com.study.testDemo;

import java.util.Random;

public class ThreadLocalDemo implements Runnable {
    private final static ThreadLocal studentLocal = new ThreadLocal(); // ThreadLocal对象在这

    public static void main(String[] agrs) {
        ThreadLocalDemo td = new ThreadLocalDemo();
        Thread t1 = new Thread(td, "a");
        Thread t2 = new Thread(td, "b");
        t1.start();
        t2.start();
    }

    public void run() {
        accessStudent();
    }

    public void accessStudent() {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println(currentThreadName + " is running!");
        Random random = new Random();
        int age = random.nextInt(100);
        System.out.println("thread " + currentThreadName + " set age to:" + age);
        Student student = getStudent(); // 每个线程都独立维护一个Student变量
        student.setAge(age);
        System.out.println("thread " + currentThreadName + " first  read age is:" + student.getAge());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("thread " + currentThreadName + " second read age is:" + student.getAge());

    }

    protected Student getStudent() {
        Student student = (Student) studentLocal.get(); // 从ThreadLocal对象中取
        if (student == null) {
            student = new Student();
            studentLocal.set(student); // 如果没有就创建一个
        }
        return student;
    }

    protected void setStudent(Student student) {
        studentLocal.set(student); // 放入ThreadLocal对象中
    }

    class Student {
        private int age;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
