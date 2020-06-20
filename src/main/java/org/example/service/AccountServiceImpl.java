package org.example.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.dao.AccountMapper;
import org.example.entity.PageResult;
import org.example.pojo.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountMapper accountMapper;

    public List<Account> selectAll() {
        return accountMapper.selectAll();
    }

    public Account selectById(String id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    public PageResult<Account> selectPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Account> accounts = (Page<Account>) accountMapper.selectAll();
        return new PageResult<Account>(accounts.getResult(),accounts.getTotal());
    }

    public List<Account> selectList(Map<String, Object> searchmap) {
        Example example = createExample(searchmap);
        return accountMapper.selectByExample(example);
    }

    public PageResult<Account> selectPage(Map<String, Object> searchmap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchmap);
        Page<Account> accounts = (Page<Account>) accountMapper.selectByExample(example);
        return new PageResult<Account>(accounts.getResult(),accounts.getTotal());
    }

    public void insert(Account account) {
        accountMapper.insert(account);
    }

    public void update(Account account) {
        accountMapper.updateByPrimaryKeySelective(account);
    }

    public void delete(String id) {
        accountMapper.deleteByPrimaryKey(id);
    }

    private Example createExample(Map<String,Object> searchmap) {
        Example example = new Example(Account.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchmap != null) {
            if (searchmap.get("accountId") != null) {
                criteria.andEqualTo("accountId", searchmap.get("accountId"));
            }
            if (searchmap.get("accountEmail") != null) {
                criteria.andEqualTo("accountEmail",searchmap.get("accountEmail"));
            }
            if (searchmap.get("accountUsingCount") != null) {
                criteria.andEqualTo("accountUsingCount",searchmap.get("accountUsingCount"));
            }
        }
        return example;
    }
}
