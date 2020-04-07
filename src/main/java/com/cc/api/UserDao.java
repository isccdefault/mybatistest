package com.cc.api;

import com.cc.api.entity.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    public List<User> getUser(Integer id);
    public List<Map> selectByCategories(String sql);

}
