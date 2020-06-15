package org.example.service;

import org.example.dao.UserMapper;
import org.example.pojo.User;

import java.util.List;

public class UserServiceImpl implements UserService{
    private UserMapper userMapper;

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }
}
