package com.in28minutes.springboot.learn_spring_boot.course.jdbc;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
// We can motify the table name as the new once  @Entity(name = "COURSE_Detail")

public class Course {
    @Id
    private long id;
    //    @Column(name = "name")
    private String name;
    //    @Column(name = "author")
    private String author;
    
    public Course( ) {
    }
    
    public Course( long id, String name, String author ) {
        this.id = id;
        this.name = name;
        this.author = author;
    }
    
    
    public String getName( ) {
        return name;
    }
    
    public void setName( String name ) {
        this.name = name;
    }
    
    public long getId( ) {
        return id;
    }
    
    public void setId( long id ) {
        this.id = id;
    }
    
    public String getAuthor( ) {
        return author;
    }
    
    public void setAuthor( String author ) {
        this.author = author;
    }
    
    @Override
    public String toString( ) {
        return "Course{" + "id=" + id + ", name='" + name + ", author='" + author + '}';
    }
}
