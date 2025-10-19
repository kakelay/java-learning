package com.in28minutes.springboot.learn_spring_boot.couse.jdbc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CourseJdbcCommandLineRunner implements CommandLineRunner {

    private final CourseJdbcRepository repository;

    public CourseJdbcCommandLineRunner(CourseJdbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        repository.insert( new Course(1,"Spring Boot","in28Minutes"));
        repository.insert( new Course(2,"Spring MVC","in28Minutes"));
        repository.insert( new Course(3,"Spring Boot and Spring MVC","in28Minutes"));


    }
}


