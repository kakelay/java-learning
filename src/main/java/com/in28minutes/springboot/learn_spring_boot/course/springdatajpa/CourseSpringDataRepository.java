package com.in28minutes.springboot.learn_spring_boot.course.springdatajpa;

import com.in28minutes.springboot.learn_spring_boot.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseSpringDataRepository extends JpaRepository<Course, Long> {


}

