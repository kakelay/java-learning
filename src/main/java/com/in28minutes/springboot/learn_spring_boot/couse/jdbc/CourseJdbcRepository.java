package com.in28minutes.springboot.learn_spring_boot.couse.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CourseJdbcRepository {

    @Autowired
    private JdbcTemplate springjdbcTemplate;

    private static final String SQL_INSERT_COURSE = "INSERT INTO course(id, name, author) VALUES (?, ?, ?)";

    public void insert(Course course) {
        springjdbcTemplate.update(SQL_INSERT_COURSE, course.getId(), course.getName(), course.getAuthor());
    }
}

