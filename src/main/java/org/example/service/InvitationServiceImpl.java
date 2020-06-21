package org.example.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.dao.AccountMapper;
import org.example.dao.InvitationMapper;
import org.example.entity.PageResult;
import org.example.pojo.Account;
import org.example.pojo.Captcha;
import org.example.pojo.Invitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
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


    @Autowired
    private AccountMapper accountMapper;
    //验证邀请码的有效性,若有效,并返回一个邮箱账号密码
    @Transactional
    public String checkInvitation(String invitationCode) {
        System.out.println("++++++++++++++++++++++++++++++++++++++");
        String msg = "";
        Map<String,Object> searchmap = new HashMap<String, Object>();
        searchmap.put("invitationCode",invitationCode);
        List<Invitation> invitations = invitationMapper.selectByExample(createExample(searchmap));
        //如果根据该邀请码在数据库中匹配不到,那么返回验证码不正确
        if(invitations.size() == 0) {
            msg = "验证码不正确";
            System.out.println(msg);
            return msg;
        }
        Invitation invitation = invitations.get(0);
        //如果该邀请码没有激活时间,说明是第一次使用,我们为他设置激活时间,匹配邮箱
        if (invitation.getInvitationActivateTime() == null) {
            invitation.setInvitationActivateTime(new Date());
            /*选择一个使用人数最少的邮箱,绑定邮箱*/
            Example example = new Example(Account.class);
            example.orderBy("accountUsingCount").asc();
            List<Account> accounts = accountMapper.selectByExample(example);
            if (accounts.size() == 0) {
                throw new RuntimeException();
            }
            Account account = accounts.get(0);
            invitation.setInvitationEmail(account.getAccountEmail());

            //记得把更新的数据写回数据库!!!
            invitationMapper.updateByPrimaryKey(invitation);

            //将被选择邮箱的使用次数+1
            account.setAccountUsingCount(account.getAccountUsingCount()+1);
            //记得把更新的数据写回数据库!!!
            accountMapper.updateByPrimaryKey(account);
            //返回与该激活码绑定的邮箱账号和密码
            String accountEmail = account.getAccountEmail();
            String accountPassword = account.getAccountPassword();
            msg = accountEmail+" "+accountPassword;
            return msg;
        }else{
            //该账户已经有激活时间,需要验证以下激活时间是否还在有效期内
            Date invitationActivateTime = invitation.getInvitationActivateTime();
            long deadlineTime = invitationActivateTime.getTime()+3 * 24 * 60 * 60 * 1000;
            long nowTime = new Date().getTime();
            if(deadlineTime<nowTime) {
                msg="邀请码已过期,请重新购买";
                return msg;
            }else{
                //返回与该激活码绑定的邮箱账号和密码,查询account表,获得与该邀请码绑定的账号,密码
                Example example = new Example(Account.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("accountEmail",invitation.getInvitationEmail());
                List<Account> accounts = accountMapper.selectByExample(example);
                if(accounts.size() == 0){
                    throw new RuntimeException("没有找到与该邀请码绑定的邮箱");
                }
                String accountEmail = accounts.get(0).getAccountEmail();
                String accountPassword = accounts.get(0).getAccountPassword();
                msg = accountEmail+" "+accountPassword;
                return msg;
            }
        }
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
