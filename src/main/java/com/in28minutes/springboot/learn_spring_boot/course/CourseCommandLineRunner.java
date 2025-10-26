package com.in28minutes.springboot.learn_spring_boot.course;

import com.in28minutes.springboot.learn_spring_boot.course.jpa.CourseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CourseCommandLineRunner implements CommandLineRunner {

//    @Autowired
//    private final CourseJdbcRepository repository;
    
    @Autowired
    private final CourseJpaRepository repository;
    
    
    @Override
    public void run( String... args ) throws Exception {
        repository.insert( new Course( 1, "Learn AWS Jpa!", "in28Minutes" ) );
        repository.insert( new Course( 2, "Learn Azure Jpa!", "in28Minutes" ) );
        repository.insert( new Course( 3, "Learn DevOps Jpa!", "in28Minutes" ) );
        repository.deleteById( 1 );
        
        System.out.println( "Record with ID=2: " + repository.findById( 2 ) );
        System.out.println( "Record with ID=3: " + repository.findById( 3 ) );
        
    }
    
    //    public CourseCommandLineRunner( CourseJdbcRepository repository ) {
    //        this.repository = repository;
    //    }
    public CourseCommandLineRunner( CourseJpaRepository repository ) {
        this.repository = repository;
    }
}


