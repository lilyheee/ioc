package com.example.course.aop.advice.interceptor;

import com.example.course.aop.advice.BeforeAfterAdvice;
import com.example.course.aop.MethodInvocation;

public class AfterAdviceInterceptor implements MethodInterceptor {
    private BeforeAfterAdvice advice;

    public AfterAdviceInterceptor(BeforeAfterAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable{
        Object returnValue = mi.proceed();
        advice.execute(mi.getMethod(), mi.getArguments(), mi.getTarget());
        return returnValue;
    }

}
