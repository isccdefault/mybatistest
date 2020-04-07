package com.cc.service.impl;


import com.cc.api.UserDao;
import com.cc.api.entity.User;
import com.cc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public List<User> getUserService(int id) {
        List<User> username = userDao.getUser(id);
        return username;
    }
    @Override
    public List<Map> selectByCategories(String sql) {
        List<Map> username = userDao.selectByCategories(sql);
        return username;
    }

}
