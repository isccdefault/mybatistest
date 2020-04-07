package com.cc.service;


import com.cc.api.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    public List<User> getUserService(int id);
    public List<Map> selectByCategories(String sql);

}