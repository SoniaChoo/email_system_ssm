package org.example.service;

import org.example.entity.PageResult;
import org.example.pojo.Account;

import java.util.List;
import java.util.Map;

public interface AccountService {
    //查找所有
    public List<Account> selectAll();

    //根据id查找
    public Account selectById(String id);

    //分页
    public PageResult<Account> selectPage(int page, int size);
    //根据条件查找
    public List<Account> selectList(Map<String,Object> searchmap);

    //根据条件和分页查询
    public PageResult<Account> selectPage(Map<String,Object> searchmap,int page,int size);

    //增加新数据
    public void insert(Account account);

    //修改数据
    public void update(Account account);

    public void delete(String id);
}
