package com.in28minutes.springboot.myfirstwebapp.todo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TodoService {
    
    private static List<Todo> todos = new ArrayList<>();
    private static int todosCount = 0;
    
    static {
        
        todos.add( new Todo( ++todosCount, "Kak Elay", "Learn AWS", LocalDate.now().plusYears( 1 ), true ) );
        todos.add( new Todo( ++todosCount, "haha Testing", "Learn DevOps", LocalDate.now().plusYears( 2 ), false ) );
        todos.add( new Todo( ++todosCount, "AH Tin Bek sloy", "Learn Full Stack Development", LocalDate.now().plusYears( 3 ), false ) );
        todos.add( new Todo( ++todosCount, "Tai Lnad", "Learn Spring Boot", LocalDate.now().plusYears( 4 ), true ) );
        
    }
    
    public List<Todo> findByUsername( String username ) {
        
        return todos;
    }
    
    public void addTodo( String username, String description, LocalDate targetDate, boolean done ) {
        Todo todo = new Todo( ++todosCount, username, description, targetDate, done );
         todos.add( todo );
    }
    
}
