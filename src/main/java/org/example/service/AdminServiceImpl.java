package org.example.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.dao.AdminMapper;
import org.example.entity.PageResult;
import org.example.pojo.Admin;
import org.example.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public List<Admin> selectAll() {
        return adminMapper.selectAll();
    }

    public Admin selectById(String id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    public List<Admin> selectList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return adminMapper.selectByExample(example);
    }

    /*分页*/
    public PageResult<Admin> selectPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<Admin> pageResult = (Page<Admin>) adminMapper.selectAll();
        return new PageResult<Admin>(pageResult.getResult(), pageResult.getTotal());
    }

    /*分页+条件*/
    public PageResult<Admin> selectPage(Map<String, Object> searchMap, int page, int size) {
        Example example = createExample(searchMap);
        PageHelper.startPage(page, size);
        Page<Admin> pageResult = (Page<Admin>) adminMapper.selectByExample(example);
        return new PageResult<Admin>(pageResult.getResult(), pageResult.getTotal());
    }

    public void insert(Admin admin) {
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    public void delete(String id) {
        adminMapper.deleteByPrimaryKey(id);
    }

    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            if (searchMap.get("adminId") != null && !"".equals(searchMap.get("adminId"))) {
                criteria.andEqualTo("adminId", searchMap.get("adminId"));
            }
            if (searchMap.get("loginName") != null && !"".equals(searchMap.get("loginName"))) {
                criteria.andEqualTo("loginName", searchMap.get("loginName"));
            }
            if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                criteria.andEqualTo("password", searchMap.get("password"));
            }
            if (searchMap.get("status") != null && !"".equals(searchMap.get("status"))) {
                criteria.andEqualTo("status", searchMap.get("status"));
            }
        }
        return example;
    }
}
