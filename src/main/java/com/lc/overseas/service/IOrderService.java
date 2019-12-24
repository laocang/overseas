package com.lc.overseas.service;

import com.lc.overseas.pojo.commentman;
import com.lc.overseas.pojo.commentman_order;

import java.util.List;

public interface IOrderService {

    int addRecord(commentman_order record);

    List<commentman_order> selectByPrimaryKey(String id);
}
