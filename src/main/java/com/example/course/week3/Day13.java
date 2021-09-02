package com.example.course.week3;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class Day13 {

    public static void main(String[] args) {

    }
}

/**
 *  design pattern
 *
 *      1. Singleton
 *          eager loading
 *          lazy loading
 *          enum
 */
//eager
class MySingleton1 {
    private final static MySingleton1 instance = new MySingleton1();

    private MySingleton1() {}

    public static MySingleton1 getInstance() {
        return instance;
    }
}
//lazy
class MySingleton2 {
    private volatile static MySingleton2 instance;
    private String name = null;
    private MySingleton2() {
    }
    public static MySingleton2 getInstance() {
        if(instance == null) {
            synchronized (MySingleton2.class) {
                if(instance == null) {
                    instance = new MySingleton2();
                }
            }
        }
        return instance;
    }
}
//enum
enum MySingleton3 {
    INSTANCE;
    private String name;
    MySingleton3() {

    }
    public String getName() {
        return this.name;
    }
}

/**
 *      factory design pattern : loose coupling
 *          class A1 implements A
 *              A a1 = getA()
 *              A a2 = getA();
 *              A a3 = getA();
 *          class A2 implements A
 *              A a1 = getA();
 *          .   ....
 *
 */
interface AFactory{}
class A1Factory implements AFactory{
    private A1Factory() {

    }
    public static Object getObj() {
        return new Object();
    }
}
class A2Factory implements  AFactory{
}

/**
 *
 */
@Data
class ReflectionStudent {
    @Inject
    private String name;
    private void print() {
        System.out.println("this is print " + name);
    }
}

class ReflectionTest {
    public static void main(String[] args) throws Throwable {
        Class<?> clazz1 = ReflectionStudent.class;
        Class<?> clazz2 = Class.forName("com.example.course.week3.ReflectionStudent");
        Class<?> clazz3 = new ReflectionStudent().getClass();
//        System.out.println(clazz1 == clazz2 && clazz2 == clazz3);

        Field[] fields = clazz1.getDeclaredFields();
        Method[] methods = clazz1.getDeclaredMethods();
        ReflectionStudent stu = (ReflectionStudent) clazz1.getDeclaredConstructor(null).newInstance();
        String defaultName = "this is default";
        if(fields[0].getAnnotations()[0].annotationType() == Inject.class) {
            fields[0].setAccessible(true);
            fields[0].set(stu, defaultName);
            System.out.println(stu);
        }
        //        System.out.println(fields[0]);
//        fields[0].setAccessible(true);
//        fields[0].set(stu, "Tom");
//        methods[0].setAccessible(true);
//        methods[0].invoke(stu);
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Inject {

}

@Data
class A {
    @Inject
    B b;
}
@Data
class B {

}
@Data
class C {
    @Inject
    A a;
    @Inject
    B b;
}

class D {

}

class Day13Starter {
    private Map<String, Object> container = new HashMap<>();
    private List<Class<?>> classList = new ArrayList<>();
    private void scan() throws Throwable{
        classList.add(A.class);
        classList.add(B.class);
        classList.add(C.class);
        container.put(A.class.getSimpleName(), new A());
        container.put(B.class.getSimpleName(), new B());
        container.put(C.class.getSimpleName(), new C());
    }

    private void register() throws Throwable{
        for(Class<?> clazz: classList) {
            Field[] fields = clazz.getDeclaredFields();
            Object inc = container.get(clazz.getSimpleName());
            for(Field field: fields) {
                field.setAccessible(true);
                Annotation[] annotations = field.getAnnotations();
                if(annotations[0].annotationType() == Inject.class) {
                    Class<?> injectTarget = field.getType();
                    String targetName = injectTarget.getSimpleName();
                    Object target = container.get(targetName);
                    field.set(inc, target);
                }
            }
        }
    }

    public static void main(String[] args) throws Throwable{
        Day13Starter starter = new Day13Starter();
        starter.scan();
        starter.register();
        System.out.println(starter.container);
    }
}

/**
 *  static proxy
 *  log before + after your method
 */
interface Day13Execution {
    void execute();
    void print();
}
class Day13ExecutionImpl implements Day13Execution{
    public void execute() {
        System.out.println("doing calculation");
    }

    @Override
    public void print() {
        System.out.println("doing print");
    }
}
//class Day13ExecutionProxy implements Day13Execution{
//    private Day13Execution ex;
//
//    public Day13ExecutionProxy(Day13Execution ex) {
//        this.ex = ex;
//    }
//    @Override
//    public void execute() {
//        System.out.println("save pre log");
//        ex.execute();
//        System.out.println("save post log");
//    }
//}
//class Day13ProxyTest {
//    public static void main(String[] args) {
//        Day13Execution ex = new Day13ExecutionImpl();
//        Day13Execution proxy = new Day13ExecutionProxy(ex);
//        proxy.execute();
//    }
//}
/**
 *
 */
class Day13DynamicProxy {
    public static void main(String[] args) {
        Day13Execution ex = new Day13ExecutionImpl();
        Day13Execution proxy = (Day13Execution) Proxy.newProxyInstance(
                Day13DynamicProxy.class.getClassLoader(),
                new Class[]{Day13Execution.class},
                new Day13InvocationHandler(ex)
        );
        proxy.execute();
        proxy.print();
    }
}

class Day13InvocationHandler implements InvocationHandler {
    private Object target;

    public Day13InvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("save pre log");
        method.invoke(target);
        System.out.println("save post log");
        return null;
    }
}


/**
 *     mapper
 *          @Mapper
 *  *      interface StuMapper {
 *  *          Object convert(Student stu, Class<?> target);
 *  *      }
 *  *
 *  *      @Inject
 *  *      StuMapper stuM; <- inject proxy instance
 *  *
 *  *      Object obj = stuM.convert(obj, class);
 *     ioc
 *          1. container
 *          2. annotation
 *          3. hardcode file scan
 *          4. reflection inject instances based on annotation
 *     aop
 *          1. @Before, @After, @Around, @AfterReturn..
 *          2.
 *             class MyAspect {
 *                  @Before("method")
 *                  public void run() {
 *                      //logic
 *                  }
 *             }
 */