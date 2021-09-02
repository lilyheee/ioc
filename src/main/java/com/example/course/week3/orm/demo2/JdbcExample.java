package com.example.course.week3.orm.demo2;

import java.sql.*;

/**
 *     DB - server(ip + port)(netty, tomcat, nodejs, jboss, weblogic...) - client
 *     blocking io server
 *     1. build connection based on request ip / port
 *     2. thread pool => assign thread
 *     3. /account post => map with AccountServlet(doGet()/doPost()) ,  /user get=> map with UserServlet
 *     4. disconnect from tcpip connection
 *     5. return thread back to thread pool
 *     non-blocking io based netty(event loop)
 *     <servlet>
 *         <path>/account</path>
 *         <class>AccountServlet</class>
 *     </servlet>
 *  JDBC flow
 *  1. define connection information.. username..db name.. url ...
 *  2. load driver(mysql / postgresql)
 *  3. connection from driver based on info
 *  4. build query => get statement
 *     statement(static query) vs preparedStatement
 *     sql injection
 *          userName :
 *          pwd      :  xx or true;select * ./ drop table..
 *          String query = "select user from table where user = " + userName + " and password = " + pwd;
 *
 *          password = "xx or true\\;select \\* ./ drop table.."
 *          password = xx or true; select..
 *
 *  5. execute statement and get result set
 *  6. read result set + close connections
 *
 *  ORM framework
 *  1. criteria query(dynamic query)
 *  2. 2nd level shared with all "connection"
 *  3. 1st level used by single "connection"
 *  4. object mapping
 *      ResultSet -> Student(firstName, lastName, age)
 *  5. @OneToMany / @ManyToMany / @OneToOne
 *      stu 1 - m myClass
 *      class Stu {
 *          firstName
 *          lastName
 *          age
 *          ..
 *          List<MyClass>
 *      }
 *
 *      myClass {
 *          Stu ref;
 *      }
 *      select * from Stu
 *      eager + lazy loading
 *  6. connection pool
 *      BlockingQueue<Connection> freeConnection..
 *      BlockingQueue<Connection> notFreeConnection..
 *  7. centralized query
 *
 *
 *  Hibernate vs  JPA(standard)
 *   session      entity manager
 *   saveOrUpdate  merge / persist
 *
 *
 *  optional homework
 *      1. student 1 - m stu_class m - 1 class
 *      2. create repository
 *          findStudentById(id)
 *               return Stu with List<Class>
 *          findStudents()
 *               only return Stu
 *          createStudent(Student stu)
 *          deleteStudentById(id)
 *
 *  deadline next Wednesday(send github link)
 *  1. implementing a mapping library(spring data jpa)
 *      @Mapper
 *      interface StuMapper {
 *          Object convert(Student stu, Class<?> target);
 *      }
 *
 *      @Inject
 *      StuMapper stuM;
 *
 *      Object obj = stuM.convert(obj, class);
 *
 *
 *  2. IOC based on JAVA
 *  3. AOP based on JAVA
 */

public class JdbcExample {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/EMP";

    //  Database credentials
    static final String USER = "username";
    static final String PASS = "password";

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            //STEP 2: Register JDBC driver -> DriverManager
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            conn.setAutoCommit(false);
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            String sql = "SELECT .... first, last, age FROM Employees where age = or name = ";
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("id");
                int age = rs.getInt("age");
                String first = rs.getString("first");
                String last = rs.getString("last");

                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Age: " + age);
                System.out.print(", First: " + first);
                System.out.println(", Last: " + last);
            }
            conn.commit();
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            //Handle errors for JDBC
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }//end main
}


