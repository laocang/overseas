package com.lc.overseas.Controller;

import com.lc.overseas.service.ICommentManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolDemo extends Thread{

    private int start;
    private int end;

    @Autowired
    private ICommentManService commentManServiceImpl;


    public ThreadPoolDemo(int start, int end){
        this.start = start;
        this.end = end;
    }

    public void run() {
        for(int i = start;i<=end;i++){
            commentManServiceImpl.requestUrl(i);
        }
    }
}
