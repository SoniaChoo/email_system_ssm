package org.example.service;

import org.example.entity.PageResult;
import org.example.entity.Result;
import org.example.pojo.Captcha;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;


public interface CaptchaService {

    //查找所有
    public List<Captcha> selectAll();

    //根据id查找
    public Captcha selectById(String id);

    //分页
    public PageResult<Captcha> selectPage(int page,int size);

    //根据条件查找
    public List<Captcha> selectList(Map<String,Object> searchmap);

    //根据条件和分页查询
    public PageResult<Captcha> selectPage(Map<String,Object> searchmap,int page,int size);

    //增加新数据
    public void insert(Captcha captcha);

    //修改数据
    public void update(Captcha captcha);

    public void delete(String id);
}
