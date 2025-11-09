package com.in28minutes.springboot.myfirstwebapp.todo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class TodoService {
    
    private static List<Todo> todos = new ArrayList<>();
    
    static {
        
        todos.add( new Todo( 1, "Kak Elay", "Learn AWS", LocalDate.now().plusYears( 1 ), false ) );
        todos.add( new Todo( 2, "Mey Gech", "Learn DevOps", LocalDate.now().plusYears( 2 ), false ) );
        todos.add( new Todo( 3, "Ou Buntha", "Learn Full Stack Development", LocalDate.now().plusYears( 3 ), false ) );
        todos.add (new Todo( 4, "Ky Chhengkong", "Learn Spring Boot", LocalDate.now().plusYears( 4 ), true ));
        
    }
    
    public List<Todo>  findByUsername( String username ) {
        return todos;
    }
    
}
