package com.example.course.test;

import com.example.course.aop.JdkDynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Starter {

    public static void main(String[] args) throws Throwable{
        TeacherAspect teacherAspect = new TeacherAspect();
        TeacherService target = new TeacherServiceImpl();
        InvocationHandler invocationHandler = new JdkDynamicProxy(teacherAspect, target);
        TeacherService proxy = (TeacherService) Proxy.newProxyInstance(Starter.class.getClassLoader(), new Class[]{TeacherService.class}, invocationHandler);
        proxy.print();
    }
}
