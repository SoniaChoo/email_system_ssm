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

    //根据条件查找
    List<Account> selectList(Map<String, Object> searchMap);

    //查询(顺序)
    public Account selectLeastUsedAccount();

    //分页
    PageResult<Account> selectPage(int page, int size);

    //根据条件和分页查询
    PageResult<Account> selectPage(Map<String, Object> searchMap, int page, int size);

    //增加新数据
    public void insert(Account account);

    //修改数据
    public void update(Account account);

    public void delete(String id);
}
