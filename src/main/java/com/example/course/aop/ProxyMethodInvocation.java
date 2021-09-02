package com.example.course.aop;

import com.example.course.aop.advice.interceptor.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyMethodInvocation implements MethodInvocation {

    private final Method method;
    private Object[] arguments;
    private final Object target;
    private final List<?> interceptors;

    private Map<Method, Method> matcher = new HashMap<>();

    private int cursor = 0;

    public ProxyMethodInvocation(Method method, Object[] arguments, Object target, List<?> interceptors) {
        this.method = method;
        this.arguments = arguments;
        this.target = target;
        this.interceptors = interceptors;
    }

    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Object getTarget() {
        return null;
    }

    @Override
    public Object proceed() throws Throwable{
        if(cursor >= interceptors.size()) {
            return executeMethod();
        }
        MethodInterceptor nextInterceptor = (MethodInterceptor) interceptors.get(cursor);
        cursor++;
        return nextInterceptor.invoke(this);
    }

    private Object executeMethod() throws Throwable{
        return method.invoke(target, arguments);
    }
}


/**
 *      @Aspect
 *      public class StudentAspect {
 *          @Before
 *          @PointCut(location = "StudentService.class.print()")
 *          @After
 *          @AfterReturn
 *          public Object logStudentInfo() {
 *
 *              System.out.println("this is pre log function")
 *          }
 *      }
 *
 *      StudentService interface proxy. print() =>
 *              1. before => StudentAspect. logStudentInfo()
 *                  print ("this is pre log function")
 *              2. real instance => instance.print();
 *              3. after => StudentAspect. logStudentInfo()
 *                  print ("this is pre log function")
 *              4. after return => ..
 *
 *       StudentService proxy = (StudentService)Proxy.newInstance(classloader, StudentService interface, invocationHandler);
 *       proxy.print()
 *
 *     MethodInvocation.proceed(); 1.
 *
 *     MethodInvocation
 *     List<?> interceptors = [beforeAdviceInterceptor, afterAdviceInterceptor, afterReturnAdviceInterceptor]
 *     int idx = 0;
 *     public Object proceed() {                    8.
 *         if(idx == advices.size()) {      2.
 *             return call real instance=> print();
 *         } else { 3.      9.
 *             return advices.get(idx++).invoke(this);   4. 10.
 *         }
 *     }
 *
 *     BeforeInterceptor
 *     public Object invoke(MethodInvocation mi) {  5.
 *         beforeAdvice();  6.
 *         return mi.proceed();  7.
 *     }
 *
 *     AfterInterceptor
 *     public Object invoke(MethodInvocation mi) { 11.
 *         Object retVal = mi.proceed();
 *         afterAdvice()
 *         return retVal;
 *     }
 *
 *     AfterReturnInterceptor
 *     public Object invoke(MethodInvocation mi) {
 *         Object retVal = mi.proceed();
 *  *      afterReturnAdvice(retVal)
 *         return result
 *     }
 *
 *    1. MethodInvocation::this  proceed()
 *          2.BeforeInterceptor
 *               3.beforeAdvice()
 *               4. return MethodInvocation::this proceed()
 *                     5.AfterInterceptor
 *                         6.result = run MethodInvocation::this proceed()
 *                                    7. AfterReturnInterceptor
 *                                          8.result = run MethodInvocation::this proceed()
 *                                                          9. call real function and return Val1
 *                                          10. afterReturnAdvice(Val1)
 *                                          11. return Val1
 *                        12. afterAdvice()
 *                        13. return Val1
 *               14. return Val1
 *    15. return Val1
 *
 *
 *
 *    hm AOP
 *      option1 : build AOP from scratch
 *      option2 :
 *              1. @AfterReturn
 *              2. @AfterThrow
 *              3. @PointerCutting("com.example.course.test.TeacherAspect")
 *              4. combine IOC + AOP
 *
 *    hm IOC
 *     1. add interface
 *  *  2. all components need to impl interface
 *  *  3. @Autowired -> inject by type
 *  *                   if we have multiple implementations of current type => throw exception
 *  *  4. @Autowired + @Qualifier("name") -> inject by bean name
 *  *  5. provide constructor injection
 *  *      @Autowired
 *  *      public xx(.. ,..) {
 *  *          .. = ..
 *  *          .. = ..
 *  *      }
 *  *  6. provide setter injection
 *  *  7. provide different injection scope / bean scope
 *  *          1. now we only supporting singleton
 *  *          2. prototype -> @Autowired => you inject a new instance
 *     8 combine IOC + AOP
 *
 *  week 4
 *  Spring IOC + AOP
 *  Spring Boot
 *  Tcp/ip + Http + Rest API + Spring MVC
 *
 *  week 5
 *  security Https / spring security / json web token
 *  Microservice , spring cloud
 *  message queue
 *  aws
 *
 *  week6
 *  day1 - 3 test + daily work / agile scrum / CI/CD
 *  day4 + day5 evaluation (30 - 45 min)
 *         rest api coding (white board)
 *         technical questions
 *
 */