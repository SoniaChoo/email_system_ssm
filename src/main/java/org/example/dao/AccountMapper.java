package org.example.dao;

import org.apache.ibatis.annotations.Select;
import org.example.pojo.Account;
import tk.mybatis.mapper.common.Mapper;

public interface AccountMapper extends Mapper<Account> {
    @Select("select " +
            "account_id as accountId, " +
            "account_nickname as accountNickname, " +
            "account_email as accountEmail, " +
            "account_password as accountPassword, " +
            "account_using_count as accountUsingCount, " +
            "account_create_time as accountCreateTime, " +
            "account_update_time as accountUpdateTime" +
            " from account order by account_using_count asc limit 1")
    Account selectLeastUsedAccount();
}
