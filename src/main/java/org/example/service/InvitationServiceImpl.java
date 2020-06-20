package org.example.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.dao.InvitationMapper;
import org.example.entity.PageResult;
import org.example.pojo.Captcha;
import org.example.pojo.Invitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class InvitationServiceImpl implements InvitationService{
    @Autowired
    private InvitationMapper invitationMapper;

    public List<Invitation> selectAll() {
        return invitationMapper.selectAll();
    }

    public Invitation selectById(String id) {
        return invitationMapper.selectByPrimaryKey(id);
    }

    public PageResult<Invitation> selectPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Invitation> invitations = (Page<Invitation>) invitationMapper.selectAll();
        return new PageResult<Invitation>(invitations.getResult(),invitations.getTotal());
    }

    public List<Invitation> selectList(Map<String, Object> searchmap) {
        Example example = createExample(searchmap);
        return invitationMapper.selectByExample(example);
    }

    public PageResult<Invitation> selectPage(Map<String, Object> searchmap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchmap);
        Page<Invitation> invitations = (Page<Invitation>) invitationMapper.selectByExample(example);
        return new PageResult<Invitation>(invitations.getResult(),invitations.getTotal());
    }

    public void insert(Invitation invitation) {
            invitationMapper.insert(invitation);
    }

    public void update(Invitation invitation) {
            invitationMapper.updateByPrimaryKeySelective(invitation);
    }

    public void delete(String id) {
            invitationMapper.deleteByPrimaryKey(id);
    }

    private Example createExample(Map<String,Object> searchmap) {
        Example example = new Example(Invitation.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchmap != null) {
            if (searchmap.get("invitationId") != null) {
                criteria.andEqualTo("invitationId", searchmap.get("invitationId"));
            }
            if (searchmap.get("invitationCode") != null) {
                criteria.andEqualTo("invitationCode",searchmap.get("invitationCode"));
            }
            if (searchmap.get("invitationLifetime") != null) {
                criteria.andEqualTo("invitationLifetime",searchmap.get("invitationLifetime"));
            }
            if (searchmap.get("invitationEmail") != null) {
                criteria.andEqualTo("invitationEmail",searchmap.get("invitationEmail"));
            }
            if (searchmap.get("invitationCaptchaCount") != null) {
                criteria.andEqualTo("captchaContent",searchmap.get("invitationCaptchaCount"));
            }
        }
        return example;
    }
}
