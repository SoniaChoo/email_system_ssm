package org.example.service;

import jdk.nashorn.internal.ir.annotations.Reference;
import org.example.dao.AdminMapper;
import org.example.pojo.Admin;
import org.example.service.AccountService;
import org.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetailServiceImpl implements UserDetailsService {


    /*这里不是autowired */
    @Autowired
    private AdminService adminService;


    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("经过了UserDetailServiceImpl");

        Map map = new HashMap<String,String>();
        map.put("loginName", s);
        map.put("status", "1");
        List<Admin> list = adminService.selectList(map);

        if (list.size() == 0) {
            throw new UsernameNotFoundException("User not authorized.");
        }
        //实际项目中应该从数据库中提取用户的角色列表
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new User(s, list.get(0).getPassword(), grantedAuthorities);
    }
}

