package com.lc.overseas.Controller;


import com.lc.overseas.pojo.users;
import com.lc.overseas.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/login")
@Controller
public class LoginController {

    @Autowired
    private IUserService userService;

    @RequestMapping("index")
    public String index(){
        return "login";
    }

    @ResponseBody
    @RequestMapping("logon")
    public users logon(HttpServletRequest request, HttpServletResponse response){
        String userName = request.getParameter("userName");
        String passWd = request.getParameter("passWd");
        users user = new users();
        try {
            user = userService.findUserByLogonIdAndPassWd(userName,passWd);
        }catch (Exception e){
            return null;
        }
        return user;
    }
}
