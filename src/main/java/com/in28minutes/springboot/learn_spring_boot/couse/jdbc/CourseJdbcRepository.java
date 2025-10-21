package com.in28minutes.springboot.learn_spring_boot.couse.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CourseJdbcRepository {

    @Autowired
    private JdbcTemplate springjdbcTemplate;

    private static final String INSERT_QUERY = "INSERT INTO course(id, name, author) VALUES (?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM course WHERE id = ?";

    public void insert(Course course) {
        springjdbcTemplate.update(INSERT_QUERY, course.getId(), course.getName(), course.getAuthor());
    }
    public void deleteById(long id) {
        springjdbcTemplate.update(DELETE_QUERY, id);
    }
}

