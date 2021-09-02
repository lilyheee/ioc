package com.example.course.aop.advice;

import java.lang.reflect.Method;

public class AspectBeforeAfterAdvice implements BeforeAfterAdvice{

    private Method matchedMethod;
    private Object aspectObj;

    public AspectBeforeAfterAdvice(Method method, Object aspectObj) {
        this.matchedMethod = method;
        this.aspectObj = aspectObj;
    }

    @Override
    public Object execute(Method method, Object[] args, Object target) throws Throwable{
        //
        this.matchedMethod.invoke(aspectObj);
        return null;
    }
}
