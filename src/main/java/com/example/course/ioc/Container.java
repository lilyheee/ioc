package com.example.course.ioc;


import com.example.course.week3.orm.demo1.Student;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *  Dependency injection => IOC (IOC container)
 *  why IOC ?
 * @Component -> mark bean -> container
 * @Autowired -> inject bean
 */
public class Container extends Exception {
    private final Map<String, Object> objectFactory = new HashMap<>();

    public static void start() throws Exception{
        Container c = new Container();
        List<Class<?>> classes = c.scan();
        c.register(classes);
        c.injectObjects(classes);
    }

    private List<Class<?>> scan() {
        return Arrays.asList(StudentRegisterService.class, StudentApplication.class, Starter.class);
    }

    private boolean register(List<Class<?>> classes) throws Exception {
        for(Class<?> clazz: classes) {
            // handle class annotations
            Annotation[] annotations = clazz.getAnnotations();
            for(Annotation a: annotations) {
                if(a.annotationType() == Component.class) {
//                    System.out.println(clazz.getSimpleName());
                    //default constructor
                    objectFactory.put(clazz.getSimpleName(), clazz.getDeclaredConstructor(null).newInstance());

                    //call autowired constructor injection
                    if (clazz.getSimpleName().equals("StudentApplication")) {
                        objectFactory.put(clazz.getSimpleName(), StudentApplication.class.getDeclaredConstructor(StudentRegisterService.class).newInstance(objectFactory.get("StudentRegisterService")));
                    }

                    // call setter injection
                    try {
                        Method m = clazz.getMethod("setter", StudentRegisterService.class);
                        Annotation[] annotation = m.getAnnotations();

                        if (annotation[0].annotationType() == Autowired.class) {
                            objectFactory.put(clazz.getSimpleName(), clazz.getDeclaredConstructor(null).newInstance());
                            StudentApplication sa = (StudentApplication)objectFactory.get(clazz.getSimpleName());
                            sa.setter((StudentRegisterService) objectFactory.get("StudentRegisterService"));
                        }
                    } catch (NoSuchMethodException exc) {
                        continue;
                    }
                }
            }




        }
        return true;
    }

    private boolean injectObjects(List<Class<?>> classes) throws Exception {
        for(Class<?> clazz: classes) {
            Field[] fields = clazz.getDeclaredFields();
            Object curInstance = objectFactory.get(clazz.getSimpleName());
            for(Field f: fields) {
                Annotation[] annotations = f.getAnnotations();
                boolean isByName = false;
                for (Annotation a : annotations) {
                    if(a.annotationType() == Qualifier.class) {
                        isByName = true;
                        //check which constructor, do by name
                        Qualifier q = (Qualifier)a;
                        Object injectInstance = objectFactory.get(q.name());
                        if (injectInstance == null) { // multiple implementations
                            throw new RuntimeException();
                        } else {
                            f.setAccessible(true);
                            f.set(curInstance, injectInstance);
                        }
                        break;
                    }
                }

                // Do by type
                if (!isByName) {
                    for(Annotation a: annotations) {
                        if(a.annotationType() == Autowired.class) {
                            Class<?> type = f.getType();
                            Object injectInstance = objectFactory.get(type.getSimpleName());
                            if (injectInstance == null) { // multiple implementations
                                throw new RuntimeException();
                            } else {
                                f.setAccessible(true);
                                f.set(curInstance, injectInstance);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}


@Component
class StudentRegisterService implements IocInterface {
    @Override
    public String toString() {
        return "this is student register service instance : " + super.toString() + "\n";
    }
}

@Component
class StudentApplication implements IocInterface {

    @Autowired
    @Qualifier(name = "StudentRegisterService")
    IocInterface studentRegisterService;

    private StudentRegisterService studentRegisterService1;

    public StudentApplication() {

    }

    //constructor injection
    @Autowired
    public StudentApplication(StudentRegisterService studentRegisterService) {
        System.out.println("StudentRegisterService arg constructor called");
        this.studentRegisterService1 = studentRegisterService;
    }

    //setter injection
    @Autowired
    public void setter(StudentRegisterService studentRegisterService) {
        System.out.println("StudentRegisterService setter called");
        this.studentRegisterService1 = studentRegisterService;
    }

    @Override
    public String toString() {
        return "StudentApplication{\n" +
                "studentRegisterService=" + studentRegisterService +
                "}\n";
    }
}

@Component
class Starter implements IocInterface {

    @Autowired
    @Qualifier(name = "StudentApplication")
    private static IocInterface studentApplication;


    @Autowired
    @Qualifier(name = "StudentRegisterService")
    private static IocInterface studentRegisterService;

    public static void main(String[] args) throws Exception{
        Container.start();
        System.out.println(studentApplication);
        System.out.println(studentRegisterService);


    }
}
/**
 *  1. add interface
 *  2. all components need to impl interface
 *  3. @Autowired -> inject by type
 *                   if we have multiple implementations of current type => throw exception
 *  4. @Autowired + @Qualifier("name") -> inject by bean name
 *  5. provide constructor injection
 *      @Autowired
 *      public xx(.. ,..) {
 *          .. = ..
 *          .. = ..
 *      }
 *  6. provide setter injection
 *  7. provide different injection scope / bean scope
 *          1. now we only supporting singleton
 *          2. prototype -> @Autowired => you inject a new instance
 */