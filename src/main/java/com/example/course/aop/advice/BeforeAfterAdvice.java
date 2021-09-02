package com.example.course.aop.advice;

import java.lang.reflect.Method;

public interface BeforeAfterAdvice extends Advice {
    Object execute(Method method, Object[] args, Object target) throws Throwable;
}
