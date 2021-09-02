package com.example.course.aop.advice.interceptor;

import com.example.course.aop.advice.BeforeAfterAdvice;
import com.example.course.aop.MethodInvocation;

public class BeforeAdviceInterceptor implements MethodInterceptor {
    private BeforeAfterAdvice advice;

    public BeforeAdviceInterceptor(BeforeAfterAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable{
        advice.execute(mi.getMethod(), mi.getArguments(), mi.getTarget());
        return mi.proceed();
    }
}
