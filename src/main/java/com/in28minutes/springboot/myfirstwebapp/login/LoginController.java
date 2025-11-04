package com.in28minutes.springboot.myfirstwebapp.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    
    @RequestMapping("login")
    public String gotoLoginPage( @RequestParam String cid, ModelMap model ) {
        model.put( "cid", cid );
        return "login";
    }
    ///124. Step 10 - Understanding **DispatcherServlet**, **Model 1**, **Model 2** and **Front Controller
}
