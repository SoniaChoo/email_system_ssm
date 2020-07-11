package org.example.controller;

import org.example.entity.PageResult;
import org.example.entity.Result;
import org.example.pojo.Account;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @GetMapping("/selectAll")
    @ResponseBody
    public List<Account> selectAll(){
        return accountService.selectAll();
    }

    @GetMapping("/selectById")
    @ResponseBody
    public Account selectById(String id) {
        return accountService.selectById(id);
    }

    /*分页*/
    @GetMapping("/selectPage")
    @ResponseBody
    public PageResult<Account> selectPage(int page, int size) {
        return accountService.selectPage(page,size);
    }

    /*分页加条件*/
    @PostMapping("/selectPage")
    @ResponseBody
    public PageResult<Account> selectPage(@RequestBody Map<String,Object> searchmap, int page, int size) {
        return accountService.selectPage(searchmap,page,size);
    }

    @PostMapping("/selectList")
    @ResponseBody
    public List<Account> selectList(@RequestBody Map<String, Object> searchmap){
        return accountService.selectList(searchmap);
    }

    @GetMapping("/selectLeastUsedAccount")
    @ResponseBody
    public Account selectOrder(){
        return accountService.selectLeastUsedAccount();
    }

    @PostMapping("/insert")
    @ResponseBody
    public Result insert(@RequestBody Account account){
        accountService.insert(account);
        return new Result();
    }

    @PostMapping("/update")
    @ResponseBody
    public Result update(@RequestBody Account account) {
        accountService.update(account);
        return new Result();
    }

    @GetMapping("/delete")
    @ResponseBody
    public Result delete(String id) {
        accountService.delete(id);
        return new Result();
    }


}
