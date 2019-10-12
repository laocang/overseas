package com.lc.overseas.service;

import com.lc.overseas.pojo.users;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IUserService {

    List findAllUser();

    users selectByPrimaryKey(int id);

    users findUserByLogonIdAndPassWd(String logonId,String passWd);
}
