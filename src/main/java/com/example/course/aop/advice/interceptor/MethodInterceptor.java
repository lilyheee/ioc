package com.example.course.aop.advice.interceptor;

import com.example.course.aop.advice.Advice;
import com.example.course.aop.MethodInvocation;

public interface MethodInterceptor extends Advice {
    Object invoke(MethodInvocation mi) throws Throwable;
}
