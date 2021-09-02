package com.example.course.week3.orm.demo3;

import org.hibernate.Transaction;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnitUtil;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

public class JPADemo {

    private DataSource getDataSource() {
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
//        dataSource.setDatabaseName("OrmDemo");
        dataSource.setUser("rq");
        dataSource.setPassword("");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/OrmDemo");
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
        em.setPackagesToScan( "demo1" );
        em.setJpaVendorAdapter( new HibernateJpaVendorAdapter() );
        em.setJpaProperties( hibernateProperties );
        em.setPersistenceUnitName( "demo-unit" );
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        em.afterPropertiesSet();
        return em.getObject();
    }

    public static void main(String[] args) {
        JPADemo jpaDemo = new JPADemo();
        DataSource dataSource = jpaDemo.getDataSource();
        Properties properties = jpaDemo.getProperties();
        EntityManagerFactory entityManagerFactory = jpaDemo.entityManagerFactory(dataSource, properties);
        EntityManager em = entityManagerFactory.createEntityManager();
        PersistenceUnitUtil unitUtil = entityManagerFactory.getPersistenceUnitUtil();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Student s = em.find(Student.class, "4");
        MyClazz c = em.find(MyClazz.class, "2");
        System.out.println("**************************************");
        System.out.println("class is loaded : " + unitUtil.isLoaded(c));
        System.out.println("collection is loaded : " + unitUtil.isLoaded(c, "stuClasses"));
        List<StuClass> stuClassList = c.getStuClasses();
        System.out.println("collection is loaded : " + unitUtil.isLoaded(c, "stuClasses"));
        System.out.println(stuClassList);
        System.out.println("collection is loaded : " + unitUtil.isLoaded(c, "stuClasses"));
        System.out.println("**************************************");
        tx.commit();
    }

    private static MyClazz createClass(String name) {
        MyClazz c =  new MyClazz();
        c.setName(name);
        return c;
    }
}


enum MySingleton {
    MY_SINGLETON;
    public final static Object obj = new Object();
    MySingleton() {

    }
}
