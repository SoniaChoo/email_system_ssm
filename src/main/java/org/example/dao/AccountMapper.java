package org.example.dao;

import org.apache.ibatis.annotations.Select;
import org.example.pojo.Account;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AccountMapper extends Mapper<Account> {
    @Select("select " +
            "account_id as accountId, " +
            "account_email as accountEmail, " +
            "account_password as accountPassword, " +
            "account_using_count as accountUsingCount" +
            " from account order by account_using_count asc limit 1")
    Account selectLeastUsedAccount();
}
