package com.in28minutes.springboot.learn_spring_boot.course.jdbc;

import com.in28minutes.springboot.learn_spring_boot.course.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CourseJdbcRepository {
    
    @Autowired
    private JdbcTemplate springjdbcTemplate;
    
    private static final String INSERT_QUERY = "INSERT INTO course(id, name, author) VALUES (?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM course WHERE id = ?";
    private static final String SELECT_QUERY = "SELECT * FROM course WHERE id = ?";
    
    public void insert( Course course ) {
        springjdbcTemplate.update( INSERT_QUERY, course.getId(), course.getName(), course.getAuthor() );
    }
    
    public void deleteById( long id ) {
        springjdbcTemplate.update( DELETE_QUERY, id );
    }
    
    public Course findById( long id ) {
        //ResultSet -> Bean => Row Mapper =>
        return springjdbcTemplate.queryForObject( SELECT_QUERY, new BeanPropertyRowMapper<>( Course.class ), id );
    }
    
}

