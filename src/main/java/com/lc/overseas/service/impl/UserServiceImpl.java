package com.lc.overseas.service.impl;


import com.lc.overseas.dao.usersMapper;
import com.lc.overseas.pojo.users;
import com.lc.overseas.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private usersMapper userDao;//这里可能会报错，但是并不会影响

    @Override
    public List findAllUser(){
        return  userDao.findAllUser();
    }

    @Override
    public users selectByPrimaryKey(int id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public users findUserByLogonIdAndPassWd(String logonId, String passWd) {
        return userDao.findUserByLogonIdAndPassWd(logonId,passWd);
    }

}
