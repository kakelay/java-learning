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
        repository.insert(1, "SpringBoot", "សុខ សារ៉ន");
        repository.insert(2, "Hibernate", "ឡាយ វុត្ថា");
        repository.insert(3, "Hibernate", "ពៅ និរ័ត្ន");
        repository.insert(4, "Hibernate", "ចាន់ ស្រីមុំ");
        repository.insert(5, "Hibernate", "មនោ ចន្ទរ័ត្ន");
        repository.insert(6, "Hibernate", "រ័ត្ន សុភាព");

    }
}


