package com.example.course.week3.orm.demo1;

import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Table(name = "student")
@Entity
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    public Student() {
    }

    public Student(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

}
