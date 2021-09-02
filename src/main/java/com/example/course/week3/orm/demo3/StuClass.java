package com.example.course.week3.orm.demo3;

import lombok.Data;

import javax.persistence.*;

@Table(name = "stu_class")
@Entity
@Data
public class StuClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "s_id")
    private Student stu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "c_id")
    private MyClazz myClazz;
}
