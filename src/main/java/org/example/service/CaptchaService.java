package org.example.service;

import org.example.entity.PageResult;
import org.example.pojo.Captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public interface CaptchaService {

    //查找所有
    public List<Captcha> selectAll();

    //根据id查找
    public Captcha selectById(String id);

    //根据条件查找
    List<Captcha> selectList(Map<String, Object> searchMap);

    //分页
    PageResult<Captcha> selectPage(int page, int size);

    //根据条件和分页查询
    PageResult<Captcha> selectPage(Map<String, Object> searchMap, int page, int size);

    //增加新数据
    public void insert(Captcha captcha);

    //修改数据
    public void update(Captcha captcha);

    public void delete(String id);

    public void log(HttpServletRequest request);
}
