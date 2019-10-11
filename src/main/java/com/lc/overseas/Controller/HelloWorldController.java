package com.lc.overseas.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloWorldController {

    @RequestMapping("hi")
    public String hi(){
        return "index";
    }

}
