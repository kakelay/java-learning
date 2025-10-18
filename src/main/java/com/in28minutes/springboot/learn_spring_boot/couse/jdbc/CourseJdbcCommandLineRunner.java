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
        repository.insert(1, "SpringBoot", "Kruy Tharin");
        repository.insert(2, "Hibernate", "Kak Elay");
        repository.insert(3, "Hibernate", "Kak Elay");
        repository.insert(4, "Hibernate", "Kak Elay");
        repository.insert(5, "Hibernate", "Kak Elay");
        repository.insert(6, "Hibernate", "Kak Elay");
    }
}


