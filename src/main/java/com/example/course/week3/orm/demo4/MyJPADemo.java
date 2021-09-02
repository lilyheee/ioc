package com.example.course.week3.orm.demo4;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.*;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

public class MyJPADemo {

    private DataSource getDataSource() {
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
//        dataSource.setDatabaseName("OrmDemo");
        dataSource.setUser("postgres");
        dataSource.setPassword("password");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/university");
        return dataSource;
    }

    private Properties getProperties() {
        final Properties properties = new Properties();
        properties.put( "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect" );
        properties.put( "hibernate.connection.driver_class", "org.postgresql.Driver" );
//        properties.put("hibernate.show_sql", "true");
        return properties;
    }

    private EntityManagerFactory entityManagerFactory(DataSource dataSource, Properties hibernateProperties ){
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan( "com/example/course/week3/orm/demo4");
        em.setJpaVendorAdapter( new HibernateJpaVendorAdapter() );
        em.setJpaProperties( hibernateProperties );
        em.setPersistenceUnitName( "demo-unit" );
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        em.afterPropertiesSet();
        return em.getObject();
    }

    public static void main(String[] args) {
        MyJPADemo jpaDemo = new MyJPADemo();
        DataSource dataSource = jpaDemo.getDataSource();
        Properties properties = jpaDemo.getProperties();
        EntityManagerFactory entityManagerFactory = jpaDemo.entityManagerFactory(dataSource, properties);
        EntityManager em = entityManagerFactory.createEntityManager();
        PersistenceUnitUtil unitUtil = entityManagerFactory.getPersistenceUnitUtil();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
//        Teacher t = em.find(Teacher.class, "1");
//        Student s = em.find(Student.class, "2");
//        Teacher_Student ts = new Teacher_Student();
//        ts.setStu(s);
//        List<Teacher_Student> list = t.getTeacher_students();
//        list.remove(0);
//        List<Teacher> teachers = em.createQuery("select t from Teacher t").getResultList();
//        System.out.println(teachers);
        List<Teacher> tList = em.createQuery("select t from Teacher t join t.teacher_students ts").getResultList();
        Teacher t = tList.get(0);
        System.out.println("**************************************");
        System.out.println("class is loaded : " + unitUtil.isLoaded(t));
        System.out.println("collection is loaded : " + unitUtil.isLoaded(t, "teacher_students"));
        em.detach(t);
        List<Teacher_Student> teacher_students = t.getTeacher_students();
        System.out.println("collection is loaded : " + unitUtil.isLoaded(teacher_students, "teacher_students"));
        System.out.println(teacher_students);
        System.out.println("collection is loaded : " + unitUtil.isLoaded(teacher_students, "teacher_students"));
        System.out.println("**************************************");
        tx.commit();
    }
}