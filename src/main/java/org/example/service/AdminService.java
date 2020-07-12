package org.example.service;

import org.example.entity.PageResult;
import org.example.pojo.Account;
import org.example.pojo.Admin;

import java.util.List;
import java.util.Map;

public interface AdminService {
    //查找所有
    public List<Admin> selectAll();

    //根据id查找
    public Admin selectById(String id);

    //根据条件查找
    List<Admin> selectList(Map<String, Object> searchMap);


    //分页
    PageResult<Admin> selectPage(int page, int size);

    //根据条件和分页查询
    PageResult<Admin> selectPage(Map<String, Object> searchMap, int page, int size);

    //增加新数据
    public void insert(Admin admin);

    //修改数据
    public void update(Admin admin);

    public void delete(String id);
}
