package com.lc.overseas.service;

import com.lc.overseas.pojo.commentman;

import java.util.List;
import java.util.Map;

public interface ICommentManService {

    int addRecord(commentman record);

    List<commentman> getAllCommentMan();

    List<commentman> getCommentMan(Map<String,Object> map);

    int updateHmd(commentman record);

    public int requestUrl(int page);
}
