package com.example.course.test;

import com.example.course.aop.annotation.After;
import com.example.course.aop.annotation.Before;

public class TeacherAspect {
    @Before("TeacherServiceImpl")
    @After("..")
    public void invoke() {
        System.out.println("aspect method");
    }
}



