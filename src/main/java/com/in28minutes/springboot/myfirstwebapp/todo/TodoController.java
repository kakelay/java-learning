package com.in28minutes.springboot.myfirstwebapp.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
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
        
        List<Todo> todos = todoService.findByUsername( "kakelay" );
        model.addAttribute( "todos", todos );
        
        return "listTodos";
    }
    
    // GET, POST ,
    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String showNewTodoPage( ModelMap model ) {
        String username = (String) model.get( "name" );
        Todo todo = new Todo( 0, username, "", LocalDate.now().plusYears( 1 ), false );
        model.put( "todo", todo );
        return "todo";
    }
    
    // GET, POST ,
    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addNewTodo( ModelMap model, Todo todo ) {
        String username = (String) model.get( "name" );
        todoService.addTodo( username, todo.getDescription(), LocalDate.now().plusYears( 1 ), false );
        return "redirect:list-todos";
    }
}
