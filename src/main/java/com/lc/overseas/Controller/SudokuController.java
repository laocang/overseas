package com.lc.overseas.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("Sudoku")
@Controller
public class SudokuController {

    @RequestMapping("index")
    public String index(HttpServletRequest request, HttpServletResponse response){
        String userId = request.getParameter("userId");
        request.setAttribute("userId",userId);
        return "Sudoku/index";
    }
}
