package com.lc.overseas.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/login")
@Controller
public class LoginController {

    @RequestMapping("index")
    public String index(){
        return "login";
    }

    @ResponseBody
    @RequestMapping("logon")
    public String logon(HttpServletRequest request, HttpServletResponse response){
        String userName = request.getParameter("userName");
        String passWd = request.getParameter("passWd");
        return "";
    }
}
