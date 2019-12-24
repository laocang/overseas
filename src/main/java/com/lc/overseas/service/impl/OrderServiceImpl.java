package com.lc.overseas.service.impl;


import com.lc.overseas.dao.commentmanMapper;
import com.lc.overseas.dao.commentman_orderMapper;
import com.lc.overseas.pojo.commentman;
import com.lc.overseas.pojo.commentman_order;
import com.lc.overseas.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "orderServiceImpl")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private commentman_orderMapper orderDao;//这里可能会报错，但是并不会影响


    @Override
    public int addRecord(commentman_order order) {
        return orderDao.insert(order);
    }

    @Override
    public List<commentman_order> selectByPrimaryKey(String id) {
        return orderDao.selectByPrimaryKey(id);
    }

}
