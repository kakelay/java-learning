package com.in28minutes.springboot.myfirstwebapp.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;


@Controller
@SessionAttributes("name")

public class TodoController {
    //http://localhost:8080/list-todos
    private TodoService todoService;
    
    @Autowired(required = true)
    public TodoController( TodoService todoService ) {
        super();
        this.todoService = todoService;
    }
    
    @RequestMapping("list-todos")
    public String listAllTodos( ModelMap model ) {
        
        List<Todo> todos = todoService.findByUsername( null );
        model.addAttribute( "todos", todos );
        
        return "listTodos";
    }
}
