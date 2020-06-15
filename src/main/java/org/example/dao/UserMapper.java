package org.example.dao;

import org.example.pojo.User;

import java.util.List;

public interface UserMapper {
    List<User> selectAllUser();
}
