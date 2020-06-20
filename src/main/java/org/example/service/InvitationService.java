package org.example.service;

import org.example.entity.PageResult;
import org.example.pojo.Invitation;

import java.util.List;
import java.util.Map;

public interface InvitationService {
    //查找所有
    public List<Invitation> selectAll();

    //根据id查找
    public Invitation selectById(String id);

    //分页
    public PageResult<Invitation> selectPage(int page, int size);
    //根据条件查找
    public List<Invitation> selectList(Map<String,Object> searchmap);

    //根据条件和分页查询
    public PageResult<Invitation> selectPage(Map<String,Object> searchmap,int page,int size);

    //增加新数据
    public void insert(Invitation invitation);

    //修改数据
    public void update(Invitation invitation);

    public void delete(String id);
}
