package org.example.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.Util;
import org.example.dao.AccountMapper;
import org.example.dao.CaptchaMapper;
import org.example.dao.InvitationMapper;
import org.example.entity.CaptchaResult;
import org.example.entity.InvitationResult;
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

import static org.example.entity.CaptchaResult.*;
import static org.example.entity.InvitationResult.*;
import static org.example.entity.InvitationResult.EXPIRED;
import static org.example.entity.InvitationResult.WRONG;

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
    public InvitationResult checkInvitation(String invitationCode) {
        String msg = "";
        InvitationResult invitationResult = new InvitationResult();

        //表单数据不能为空
        if (invitationCode == null||"".equals(invitationCode) ) {
            msg = "邀请码不能为空";
            invitationResult.setCode(NOVALUE);
            invitationResult.setMsg(msg);
            return invitationResult;
        }
        Map<String,Object> searchmap = new HashMap<String, Object>();
        searchmap.put("invitationCode",invitationCode);
        List<Invitation> invitations = invitationMapper.selectByExample(createExample(searchmap));

        //如果根据该邀请码在数据库中匹配不到,那么返回邀请码码不正确
        if(invitations.size() == 0) {
            msg = "邀请码不正确";
            invitationResult.setCode(WRONG);
            invitationResult.setMsg(msg);
            return invitationResult;
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
            Map<String, String> map = new HashMap<String, String>();
            map.put("accountEmail",accountEmail);
            map.put("accountPassword",accountPassword);
            invitationResult.setData(map);
            return invitationResult;
        }else{

            //该账户已经有激活时间,需要验证一下激活时间是否还在有效期内
            Date invitationActivateTime = invitation.getInvitationActivateTime();
            Integer lifetime = invitation.getInvitationLifetime();
            long deadlineTime = invitationActivateTime.getTime()+lifetime * 24 * 60 * 60 * 1000;
            long nowTime = new Date().getTime();

            //如果时间超过有效期
            if(deadlineTime<nowTime) {

                //邀请码已经失效,邮箱使用次数-1,然后不要忘记把更新的数据写回数据库
                Example example = new Example(Account.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("accountEmail",invitation.getInvitationEmail());
                List<Account> accounts = accountMapper.selectByExample(example);
                if (accounts.size()!=1){

                    //邀请码已经解绑邮箱了,用户仍然去请求
                    invitationResult.setCode(EXPIRED);
                    msg="邀请码已过期,请重新购买";
                    invitationResult.setMsg(msg);
                    return invitationResult;
                }
                Account account = accounts.get(0);
                account.setAccountUsingCount(account.getAccountUsingCount()-1);
                accountMapper.updateByPrimaryKey(account);

                //邀请码已经失效,我们要把与邀请码绑定的邮箱解绑,然后不要忘记把更新的数据写回数据库
                invitation.setInvitationEmail(null);
                invitationMapper.updateByPrimaryKey(invitation);

                //Q:?邀请码需要在数据库中删除码?
                invitationResult.setCode(EXPIRED);
                msg="邀请码已过期,请重新购买";
                invitationResult.setMsg(msg);
                return invitationResult;
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("accountEmail",accountEmail);
                map.put("accountPassword",accountPassword);
                invitationResult.setData(map);
                return invitationResult;
            }
        }
    }

    @Autowired
    private CaptchaMapper captchaMapper;

    @Transactional
    public CaptchaResult searchCaptcha(String invitationCode, String captchaTo) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(invitationCode);
        System.out.println(captchaTo);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        CaptchaResult captchaResult = new CaptchaResult();
        InvitationResult invitationResult = checkInvitation(invitationCode);
        //1代表邀请码不正确Wrong,2代表邀请码失效Expired
        if (invitationResult.getCode() == WRONG) {
            captchaResult.setCode(WRONG);
            captchaResult.setMsg(invitationResult.getMsg());
            return captchaResult;
        }else if (invitationResult.getCode()==EXPIRED) {
            captchaResult.setCode(EXPIRED);
            captchaResult.setMsg(invitationResult.getMsg());
            return captchaResult;
        }

        Example example = new Example(Invitation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("invitationCode",invitationCode);
        List<Invitation> invitations = invitationMapper.selectByExample(example);
        if(invitations.size() != 1) {
            throw new RuntimeException("没有找到对应的邀请码,或者邀请码不唯一");
        }
        Invitation invitation = invitations.get(0);

        //验证输入的邮箱是否与邀请码绑定的邮箱一致
        if (!(captchaTo.equals(invitation.getInvitationEmail()))) {
            captchaResult.setCode(BINDING);
            captchaResult.setMsg("请勿修改邮箱的输入值");
            return captchaResult;
        }

        //如果时间相较于前一天第一次验证时间过了一天,将第一次验证时间更新为现在的时间,验证次数设为0;
        //如果是第一次请求验证码,需要更新invitation表中验证时间和验证次数
        if (invitation.getInvitationFirstCaptchaTime()==null || invitation.getInvitationFirstCaptchaTime().getTime()+1 * 24 * 60 * 60 * 1000< new Date().getTime()) {
            invitation.setInvitationFirstCaptchaTime(new Date());
            invitation.setInvitationCaptchaCount(0);
            invitationMapper.updateByPrimaryKey(invitation);
        }else {
            //验证次数加1
            invitation.setInvitationCaptchaCount(invitation.getInvitationCaptchaCount() + 1);
            invitationMapper.updateByPrimaryKey(invitation);
        }

        //判断在一天之内请求获取验证码次数是否不超过5次
        if (invitation.getInvitationCaptchaCount() > 5) {
            captchaResult.setCode(EXCEEDING);
            captchaResult.setMsg("今天获取验证码次数已经达到5次上限");
            return captchaResult;
        }

        //确认了邀请码的正确性,有效性,和绑定邮箱一致,当天验证次数没有超过上限后,我们去查找邮件,查找条件是收到后不超过一个小时,未读,按收到时间降序排序
        Example example1 = new Example(Captcha.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("captchaTo",captchaTo);
//            criteria1.andGreaterThan("captchaReceiveTime",new Date().getTime()-1 * 60 * 60 * 1000);
       // criteria1.andEqualTo("captchaRead",0);
        example1.orderBy("captchaReceiveTime").desc();
        List<Captcha> captchas = captchaMapper.selectByExample(example1);

        //邮箱表可能还没有对应邮箱的邮件,也有可能有很多封,当有很多封的时候,我们把最新的一封返回
        if(captchas.size() == 0) {
            captchaResult.setCode(NOTRECEIVE);
            captchaResult.setMsg("验证码还未收到");
            return captchaResult;
        }
        Captcha captcha = captchas.get(0);

        //把验证码表中对应的captchaRead设为已读;
        captcha.setCaptchaRead(1);
        captchaMapper.updateByPrimaryKey(captcha);

        Map<String, String> map = new HashMap<String, String>();

        map.put("captchaCode",captcha.getCaptchaCode());
        captchaResult.setData(map);
        return captchaResult;
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
