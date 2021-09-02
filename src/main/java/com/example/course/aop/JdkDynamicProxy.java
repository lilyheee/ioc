package com.example.course.aop;

import com.example.course.aop.advice.AspectBeforeAfterAdvice;
import com.example.course.aop.advice.BeforeAfterAdvice;
import com.example.course.aop.annotation.After;
import com.example.course.aop.annotation.Before;
import com.example.course.aop.advice.interceptor.AfterAdviceInterceptor;
import com.example.course.aop.advice.interceptor.BeforeAdviceInterceptor;
import com.example.course.aop.advice.interceptor.MethodInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//generating teacher proxy
public class JdkDynamicProxy implements InvocationHandler {

    private Object aspect; //teacher aspect
    private Object target; //teacher target

    public JdkDynamicProxy(Object aspect, Object target) {
        this.aspect = aspect;
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<MethodInterceptor> list = getInterceptor(aspect);
        ProxyMethodInvocation proxyMethodInvocation = new ProxyMethodInvocation(method, args, target, list);
        return proxyMethodInvocation.proceed();
    }

    private List<MethodInterceptor> getInterceptor(Object aspectObj) {
        Class<?> clazz = aspect.getClass();
        if(clazz == null) {
            return null;
        }
        Method[] methods = clazz.getDeclaredMethods();
        List<MethodInterceptor> list = new ArrayList<>();
        for(Method mi: methods) {
            Annotation[] annotations = mi.getDeclaredAnnotations();
            for(Annotation an: annotations) {
                if(an.annotationType() == Before.class) {
                    BeforeAfterAdvice before = new AspectBeforeAfterAdvice(mi, aspectObj);
                    MethodInterceptor methodInterceptor = new BeforeAdviceInterceptor(before);
                    list.add(methodInterceptor);
                } else if(an.annotationType() == After.class) {
                    BeforeAfterAdvice after = new AspectBeforeAfterAdvice(mi, aspectObj);
                    MethodInterceptor methodInterceptor = new AfterAdviceInterceptor(after);
                    list.add(methodInterceptor);
                }
            }
        }
        return list;
    }
}
