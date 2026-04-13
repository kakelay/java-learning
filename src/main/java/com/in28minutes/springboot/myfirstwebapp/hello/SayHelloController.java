package com.in28minutes.springboot.myfirstwebapp.hello;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Locale;

@Controller
@SessionAttributes("name")
public class SayHelloController {
    
    private MessageSource messageSource;
    
    public SayHelloController( MessageSource messageSource ) {
        super();
        this.messageSource = messageSource;
    }
    
    @GetMapping("/hello-world")
    @ResponseBody
    public String helloWorld( ) {
        return "hello-world";
    }
    
    //http://localhost:8080/say-hello
    @RequestMapping("say-hello")
    @ResponseBody
    public String sayHello( ) {
        return "Hello!!! what are you learning today ?";
    }
    
    @RequestMapping("say-hello-html")
    @ResponseBody
    public String sayHelloHtml( ) {
        StringBuffer sb = new StringBuffer();
        sb.append( "<html>" );
        sb.append( "<head>" );
        sb.append( "<title>My first HTML Page Changed</title>" );
        sb.append( "</head>" );
        sb.append( "<body>" );
        sb.append( "<h1> My Body for HTML </h1>" );
        sb.append( "</body>" );
        sb.append( "</html>" );
        return sb.toString();
        
        
    }
    
    //  say-Hello-jsp => sayHelloJsp.jsp
    @RequestMapping("say-hello-jsp")
    @ResponseBody
    public String sayHelloJsp( ) {
        return "sayHello";
    }
    
    @GetMapping("/hello-world-helloWorldInternationalized")
    @ResponseBody
    public String helloWorldInternationalized( Locale locale ) {
        
        Locale locale1 = LocaleContextHolder.getLocale();
        
        return messageSource.getMessage( "good.morning.message", null, "Default Message", locale );
        
//        return "hello World Internationalized v2";
    }
    
}
