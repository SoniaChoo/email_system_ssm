package org.example.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.log4j.Logger;
import org.example.dao.AccountMapper;
import org.example.entity.PageResult;
import org.example.pojo.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {
    String accountId = "accountId";
    String accountEmail = "accountEmail";
    String accountPassword = "accountPassword";
    String accountUsingCount = "accountUsingCount";

    static Logger logger = Logger.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    public List<Account> selectAll() {
        return accountMapper.selectAll();
    }

    public Account selectById(String id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    public List<Account> selectList(Map<String, Object> searchMap) {
        return accountMapper.selectByExample(createExample(searchMap));
    }

    /*选择当前使用最少的一个邮箱账号*/
    public Account selectLeastUsedAccount() {
        Account account = accountMapper.selectLeastUsedAccount();
        if (account == null) {
            logger.error("no more account to use. 系统中没有账户。");
            throw new RuntimeException();
        }
        return account;
    }

    public PageResult<Account> selectPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<Account> accounts = (Page<Account>) accountMapper.selectAll();
        return new PageResult<Account>(accounts.getResult(), accounts.getTotal());
    }

    public PageResult<Account> selectPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        Page<Account> accounts = (Page<Account>) accountMapper.selectByExample(example);
        return new PageResult<Account>(accounts.getResult(),accounts.getTotal());
    }

    public void insert(Account account) {
        if (account.getAccountUsingCount() == null) {
            account.setAccountUsingCount(0);
        }
        accountMapper.insert(account);
    }

    public void update(Account account) {
        accountMapper.updateByPrimaryKeySelective(account);
    }

    public void delete(String id) {
        accountMapper.deleteByPrimaryKey(id);
    }

    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Account.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            if (searchMap.get(accountId) != null) {
                criteria.andEqualTo(accountId, searchMap.get(accountId));
            }
            if (searchMap.get(accountEmail) != null) {
                criteria.andEqualTo(accountEmail, searchMap.get(accountEmail));
            }
            if (searchMap.get(accountPassword) != null) {
                criteria.andEqualTo(accountPassword, searchMap.get(accountPassword));
            }
            if (searchMap.get(accountUsingCount) != null) {
                criteria.andEqualTo(accountUsingCount, searchMap.get(accountUsingCount));
            }
        }
        return example;
    }
}
