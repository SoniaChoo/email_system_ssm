package org.example.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.log4j.Logger;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.entity.Result.*;

@Service
public class InvitationServiceImpl implements InvitationService {
    String invitationId = "invitationId";
    String invitationCode = "invitationCode";
    String invitationLifetime = "invitationLifetime";
    String invitationEmail = "invitationEmail";
    String invitationCaptchaCount = "invitationCaptchaCount";

    static Logger logger = Logger.getLogger(AccountServiceImpl.class);

    @Autowired
    private InvitationMapper invitationMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CaptchaMapper captchaMapper;


    public List<Invitation> selectAll() {
        return invitationMapper.selectAll();
    }

    public Invitation selectById(String id) {
        return invitationMapper.selectByPrimaryKey(id);
    }

    public List<Invitation> selectList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return invitationMapper.selectByExample(example);
    }

    public PageResult<Invitation> selectPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<Invitation> invitations = (Page<Invitation>) invitationMapper.selectAll();
        return new PageResult<Invitation>(invitations.getResult(), invitations.getTotal());
    }

    public PageResult<Invitation> selectPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        Page<Invitation> invitations = (Page<Invitation>) invitationMapper.selectByExample(example);
        return new PageResult<Invitation>(invitations.getResult(), invitations.getTotal());
    }

    public void insert(Invitation invitation) {
        if (invitation.getInvitationCaptchaCount() == null) {
            invitation.setInvitationCaptchaCount(0);
        }
        invitationMapper.insert(invitation);
    }

    public void update(Invitation invitation) {
            invitationMapper.updateByPrimaryKeySelective(invitation);
    }

    public void delete(String id) {
            invitationMapper.deleteByPrimaryKey(id);
    }

    //验证邀请码的有效性,若有效,并返回一个邮箱账号密码
    @Transactional
    public InvitationResult checkInvitation(String invitationCode) {
        invitationCode = invitationCode.trim();
        InvitationResult invitationResult = new InvitationResult();
        Invitation invitation;
        Account account;
        long deadlineTime;
        Date nowDate = new Date(); // 系统当前时间

        //表单数据不能为空
        if ("".equals(invitationCode)) {
            invitationResult.setCode(NOVALUE);
            invitationResult.setMsg("邀请码不能为空");
            return invitationResult;
        }

        Map<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put(this.invitationCode, invitationCode);
        List<Invitation> invitations = invitationMapper.selectByExample(createExample(searchMap));

        //如果根据该邀请码在数据库中匹配不到,那么返回邀请码码不正确
        if (invitations.size() == 0) {
            invitationResult.setCode(WRONG);
            invitationResult.setMsg("邀请码不正确");
            return invitationResult;
        }
        //如果该邀请码在数据库中有重复, 返回错误
        if (invitations.size() != 1) {
            invitationResult.setCode(WRONG);
            invitationResult.setMsg("邀请码不正确");
            // logout
            logger.error("invitation code duplicate. 邀请码有重复。invitationCode : " + invitationCode);
            return invitationResult;
        }
        invitation = invitations.get(0);

        //如果该邀请码没有激活时间,说明是第一次使用,我们为他设置激活时间,匹配邮箱
        if (invitation.getInvitationActivateTime() == null) {
            invitation.setInvitationActivateTime(nowDate);

            /*选择一个使用人数最少的邮箱,绑定邮箱, 更新信息*/
            account = getAccount(invitation);
            invitation.setInvitationEmail(account.getAccountEmail());
            invitationMapper.updateByPrimaryKey(invitation);

            Integer lifetime = invitation.getInvitationLifetime();
            deadlineTime = nowDate.getTime() + lifetime * 24L * 60L * 60L * 1000L;
        } else {
            //该账户已经有激活时间,需要验证一下激活时间是否还在有效期内
            Date invitationActivateTime = invitation.getInvitationActivateTime();
            Integer lifetime = invitation.getInvitationLifetime();
            deadlineTime = invitationActivateTime.getTime() + lifetime * 24L * 60L * 60L * 1000L;

            //如果时间超过有效期, 邀请码失效
            if (deadlineTime < nowDate.getTime()) {
//                // TODO:过期邀请码解绑邮箱, 使用定时器
//                //邀请码已经失效,邮箱使用次数-1,然后不要忘记把更新的数据写回数据库
//                Example example = new Example(Account.class);
//                Example.Criteria criteria = example.createCriteria();
//                criteria.andEqualTo(account,invitation.getInvitationEmail());
//                List<Account> accounts = accountMapper.selectByExample(example);
//                if (accounts.size()!=1){
//
//                    //邀请码已经解绑邮箱了,用户仍然去请求
//                    invitationResult.setCode(EXPIRED);
//                    msg="邀请码已过期,请重新购买";
//                    invitationResult.setMsg(msg);
//                    return invitationResult;
//                }
//                Account account = accounts.get(0);
//                account.setAccountUsingCount(account.getAccountUsingCount()-1);
//                accountMapper.updateByPrimaryKey(account);
//
//                //邀请码已经失效,我们要把与邀请码绑定的邮箱解绑,激活时间不要置为null, 然后不要忘记把更新的数据写回数据库
//                invitation.setInvitationEmail(null);
//                invitationMapper.updateByPrimaryKey(invitation);

                invitationResult.setCode(EXPIRED);
                invitationResult.setMsg("邀请码已过期,请联系客服重新购买");
                return invitationResult;
            } else {
                // 邀请码有效, 检查邮箱绑定
                //返回与该激活码绑定的邮箱账号和密码,查询account表,获得与该邀请码绑定的账号,密码
                if (invitation.getInvitationEmail() == null) {
                    account = getAccount(invitation);
                    invitation.setInvitationEmail(account.getAccountEmail());
                    invitationMapper.updateByPrimaryKey(invitation);
                }
                Example example = new Example(Account.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("accountEmail", invitation.getInvitationEmail());
                List<Account> accounts = accountMapper.selectByExample(example);
                if (accounts.size() == 0) {
                    logger.error("related account not found. 未找到绑定的邮箱。" +
                            "invitationCode : " + invitationCode +
                            ", account : " + invitation.getInvitationEmail());
                    throw new RuntimeException("没有找到与该邀请码绑定的邮箱");
                }
                //如果该邮箱账号在数据库中有重复, 返回错误
                if (accounts.size() != 1) {
                    invitationResult.setCode(WRONG);
                    invitationResult.setMsg("邀请码不正确");
                    // logout
                    logger.error("email account duplicate. 邮箱账号有重复。" +
                            "invitationCode : " + invitationCode +
                            ", account : " + invitation.getInvitationEmail());
                    return invitationResult;
                }
                account = accounts.get(0);
            }
        }
        return successResult(invitationResult, invitation, account, deadlineTime);
    }

    @Transactional
    public CaptchaResult searchCaptcha(String invitationCode, String captchaTo) {
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
        criteria.andEqualTo(this.invitationCode, invitationCode);
        List<Invitation> invitations = invitationMapper.selectByExample(example);
        if(invitations.size() != 1) {
            throw new RuntimeException("没有找到对应的邀请码,或者邀请码不唯一");
        }
        Invitation invitation = invitations.get(0);

        /*验证码获取次数*/
        int remainCount = TOTALCOUNT - invitation.getInvitationCaptchaCount();
        if(remainCount < 0) {
            remainCount = 0;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("remainCount",remainCount+"");
        captchaResult.setData(map);

        //验证输入的邮箱是否与邀请码绑定的邮箱一致
        if (!(captchaTo.equals(invitation.getInvitationEmail()))) {
            captchaResult.setCode(BINDING);
            captchaResult.setMsg("请勿修改邮箱的输入值");
            return captchaResult;
        }

        //如果时间相较于前一天第一次验证时间过了一天,将第一次验证时间更新为现在的时间,验证次数设为0;
        //如果是第一次请求验证码,需要更新invitation表中验证时间和验证次数
        if (invitation.getInvitationFirstCaptchaTime()==null || invitation.getInvitationFirstCaptchaTime().getTime()+24 * 60 * 60 * 1000< new Date().getTime()) {
            invitation.setInvitationFirstCaptchaTime(new Date());
            invitation.setInvitationCaptchaCount(0);
            invitationMapper.updateByPrimaryKey(invitation);
        }

        //验证次数加1
//        invitation.setInvitationCaptchaCount(invitation.getInvitationCaptchaCount() + 1);
//        invitationMapper.updateByPrimaryKey(invitation);

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
        criteria1.andLike("captchaFrom","%"+"@baidu.com"+"%");
        //criteria1.andGreaterThan("captchaReceiveTime",new Date().getTime()-1 * 60 * 60 * 1000);
        criteria1.andEqualTo("captchaRead",0);
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
        long oneHourAgo = new Date().getTime()-60 * 60 * 1000;
        if (captcha.getCaptchaReceiveTime().getTime()<oneHourAgo){
            captchaResult.setCode(OUTDATED);
            captchaResult.setMsg("验证码已过期");
            return captchaResult;
        }


        //成功的时候添加数据
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("captchaCode",captcha.getCaptchaCode());

        //成功的时候,验证次数再加1
        invitation.setInvitationCaptchaCount(invitation.getInvitationCaptchaCount() + 1);
        invitationMapper.updateByPrimaryKey(invitation);

        /*验证码剩余可获取次数*/
         remainCount--;
        if(remainCount < 0) {
            remainCount = 0;
        }
        map2.put("remainCount",remainCount+"");
        captchaResult.setData(map2);
        return captchaResult;
    }

    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Invitation.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            if (searchMap.get(invitationId) != null) {
                criteria.andEqualTo(invitationId, searchMap.get(invitationId));
            }
            if (searchMap.get(invitationCode) != null) {
                criteria.andEqualTo(invitationCode, searchMap.get(invitationCode));
            }
            if (searchMap.get(invitationLifetime) != null) {
                criteria.andEqualTo(invitationLifetime, searchMap.get(invitationLifetime));
            }
            if (searchMap.get(invitationEmail) != null) {
                criteria.andEqualTo(invitationEmail, searchMap.get(invitationEmail));
            }
            if (searchMap.get(invitationCaptchaCount) != null) {
                criteria.andEqualTo(invitationCaptchaCount, searchMap.get(invitationCaptchaCount));
            }
        }
        return example;
    }

    private Account getAccount(Invitation invitation) {
        /*选择一个使用人数最少的邮箱,绑定邮箱*/
        Account account = accountMapper.selectLeastUsedAccount();
        if (account == null) {
            logger.error("failed to bind account. 绑定邮箱失败。");
            throw new RuntimeException();
        }
        //将被选择邮箱的使用次数+1
        account.setAccountUsingCount(account.getAccountUsingCount() == null ? 1 : account.getAccountUsingCount() + 1);
        //记得把更新的数据写回数据库!!!
        accountMapper.updateByPrimaryKey(account);

        return account;
    }

    private InvitationResult successResult(InvitationResult invitationResult, Invitation invitation, Account account, long deadlineTime) {
        //返回与该激活码绑定的邮箱账号和密码
        String accountEmail = account.getAccountEmail();
        String accountPassword = account.getAccountPassword();
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountPassword", accountEmail);
        map.put("accountPassword", accountPassword);
        // TODO: 带上邀请码信息
        map.put("invitationCode", invitation.getInvitationCode());
        //把到期时间带上
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd- HH:mm:ss");
        Date date = new Date(deadlineTime);
        map.put("invitationDeadlinetime", formatter.format(date));
        //把当天获取验证码剩余次数带上
        int remainCount = TOTALCOUNT - invitation.getInvitationCaptchaCount();
        if (remainCount < 0) {
            remainCount = 0;
        }
        map.put("remainCount", remainCount + "");
        invitationResult.setData(map);
        return invitationResult;
    }
}
