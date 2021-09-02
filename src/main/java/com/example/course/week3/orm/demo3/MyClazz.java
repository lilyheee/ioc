package com.example.course.week3.orm.demo3;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "myclazz")
@Entity
@Data
public class MyClazz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "myClazz", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StuClass> stuClasses = new ArrayList<>();

    public List<StuClass> getStuClasses() {
        return stuClasses;
    }

    public void setStuClasses(StuClass sc) {
        this.stuClasses.add(sc);
    }
}
