package org.example.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.example.dao.AccountMapper;
import org.example.dao.CaptchaMapper;
import org.example.dao.InvitationMapper;
import org.example.entity.CaptchaResult;
import org.example.entity.InvitationResult;
import org.example.entity.PageResult;
import org.example.entity.Result;
import org.example.pojo.Account;
import org.example.pojo.Captcha;
import org.example.pojo.Invitation;
import org.example.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public int insert(Invitation invitation) {
        if (invitation.getInvitationId() == null) {
            invitation.setInvitationId(UUID.randomUUID().toString());
        }
        if (invitation.getInvitationCaptchaCount() == null) {
            invitation.setInvitationCaptchaCount(0);
        }
        if(invitation.getInvitationCreateTime()==null) {
            invitation.setInvitationCreateTime(new Date());
        }
        if(invitation.getInvitationUpdateTime()==null) {
            invitation.setInvitationUpdateTime(new Date());
        }
        return invitationMapper.insert(invitation);
    }

    public InvitationResult insertMany(int lifetime, int count) {
        List<String> codes = Util.generateInvitationCode(lifetime + "tian", count);
        int successCount = 0;
        for (String code : codes) {
            Invitation invitation = new Invitation();
            invitation.setInvitationCode(code);
            invitation.setInvitationLifetime(lifetime);
            invitation.setInvitationCreateTime(new Date());
            int returnCode = insert(invitation);
            successCount += returnCode;
        }

        InvitationResult result = new InvitationResult();
        Map<String, String> data = new HashMap<String, String>();
        data.put("successCount", "" + successCount);
        result.setData(data);
        result.setRows(codes);
        return result;
    }

    public void update(Invitation invitation) {
        if(invitation.getInvitationCreateTime()==null) {
            invitation.setInvitationCreateTime(new Date());
        }
        invitation.setInvitationUpdateTime(new Date());
        invitationMapper.updateByPrimaryKeySelective(invitation);
    }

    public void delete(String id) {
        invitationMapper.deleteByPrimaryKey(id);
    }

    public void unbindEmail(String email) {
        Example example = new Example(Invitation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(invitationEmail, email);
        List<Invitation> invitations = invitationMapper.selectByExample(example);

        for (Invitation i : invitations) {
            i.setInvitationEmail(null);
            invitationMapper.updateByPrimaryKey(i);
        }
    }

    //验证邀请码的有效性,若有效,并返回一个邮箱账号密码
    @Transactional
    public InvitationResult checkInvitation(String code) {
        code = code.trim();
        InvitationResult invitationResult = new InvitationResult();
        Invitation invitation;
        Account account;
        long deadlineTime; //该邀请码的过期时间
        Date nowDate = new Date(); // 系统当前时间

        //表单数据不能为空
        if ("".equals(code)) {
            invitationResult.setCode(NOVALUE);
            invitationResult.setMsg("邀请码不能为空");
            return invitationResult;
        }

        Map<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put(this.invitationCode, code);
        List<Invitation> invitations = invitationMapper.selectByExample(createExample(searchMap));

        //如果根据该邀请码在数据库中匹配不到,那么返回邀请码码不正确
        if (invitations.size() == 0) {
            invitationResult.setCode(WRONG);
            invitationResult.setMsg("邀请码不正确");
            return invitationResult;
        } else if (invitations.size() != 1) { //如果该邀请码在数据库中有重复, 返回错误
            invitationResult.setCode(WRONG);
            invitationResult.setMsg("邀请码不正确");
            // logout
            logger.error("invitation code duplicate. 邀请码有重复。invitationCode : " + code);
            return invitationResult;
        }
        invitation = invitations.get(0);

        //如果该邀请码没有激活时间,说明是第一次使用,我们为他设置激活时间,匹配邮箱
        if (invitation.getInvitationActivateTime() == null) {
            invitation.setInvitationActivateTime(nowDate);

            /*选择一个使用人数最少的邮箱,绑定邮箱, 更新信息*/
            account = getAccount(invitation);
            invitation.setInvitationEmail(account.getAccountEmail());
            invitation.setInvitationCaptchaCount(0);
            invitationMapper.updateByPrimaryKey(invitation);

            Integer lifetime = invitation.getInvitationLifetime();
            deadlineTime = nowDate.getTime() + lifetime * 24L * 60L * 60L * 1000L;
        } else {
            //该账户已经有激活时间,需要验证一下激活时间是否还在有效期内
            Date invitationActivateTime = invitation.getInvitationActivateTime();
            Integer lifetime = invitation.getInvitationLifetime();
            deadlineTime = invitationActivateTime.getTime() + lifetime * 24L * 60L * 60L * 1000L;

            //如果时间超过有效期, 邀请码失效, 直接返回
            if (deadlineTime < nowDate.getTime()) {
                // TODO:过期邀请码解绑邮箱, 使用定时器
                invitationResult.setCode(EXPIRED);
                invitationResult.setMsg("邀请码已过期,请联系客服重新购买");
                return invitationResult;
            }

            // 邀请码有效, 检查邮箱绑定
            if (invitation.getInvitationEmail() == null) { // 增加邮箱检查, 增强健壮性
                account = getAccount(invitation);
                invitation.setInvitationEmail(account.getAccountEmail());
                invitation.setInvitationCaptchaCount(0);
                invitationMapper.updateByPrimaryKey(invitation);
            }

            //返回与该激活码绑定的邮箱账号和密码,查询account表,获得与该邀请码绑定的账号,密码
            Example example = new Example(Account.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("accountEmail", invitation.getInvitationEmail());
            List<Account> accounts = accountMapper.selectByExample(example);
            if (accounts.size() == 0) {
                invitationResult.setCode(WRONG);
                invitationResult.setMsg("邀请码不正确");
                logger.error("related account not found. 未找到绑定的邮箱。" +
                        "invitationCode : " + code +
                        ", account : " + invitation.getInvitationEmail());
                return invitationResult;

            } else if (accounts.size() != 1) { //如果该邮箱账号在数据库中有重复, 返回错误
                invitationResult.setCode(WRONG);
                invitationResult.setMsg("邀请码不正确");
                // logout
                logger.error("email account duplicate. 邮箱账号有重复。" +
                        "invitationCode : " + code +
                        ", account : " + invitation.getInvitationEmail());
                return invitationResult;
            }
            account = accounts.get(0);
        }
        return successResult(invitationResult, invitation, account, deadlineTime);
    }

    @Transactional
    public CaptchaResult searchCaptcha(String code, String accountEmail) {
        CaptchaResult captchaResult = new CaptchaResult();
        InvitationResult invitationResult = checkInvitation(code);
        if (invitationResult.getCode() != RIGHT) {
            captchaResult.setCode(WRONG);
            captchaResult.setMsg("邀请码不正确");
            logger.error("invitation outdated. 邀请码已过期。" +
                    "invitationCode : " + code +
                    ", account : " + accountEmail);
            return captchaResult;
        }

        Invitation invitation;
        Captcha captcha;
        Date nowDate = new Date(); // 系统当前时间
        Map<String, String> map = new HashMap<String, String>();

        Example example = new Example(Invitation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(this.invitationEmail, accountEmail);
        criteria.andEqualTo(this.invitationCode, code);
        List<Invitation> invitations = invitationMapper.selectByExample(example);
        if (invitations.size() == 0) {
            captchaResult.setCode(BINDING);
            captchaResult.setMsg("请勿修改邮箱的输入值");
            logger.error("invitation and account not match. 邀请码与邮箱不匹配。" +
                    "invitationCode : " + code +
                    ", account : " + accountEmail);
            return captchaResult;
        } else if (invitations.size() != 1) {
            captchaResult.setCode(WRONG);
            captchaResult.setMsg("邀请码不正确");
            logger.error("invitation duplicate. 邀请码有重复。" +
                    "invitationCode : " + code +
                    ", account : " + accountEmail);
            return captchaResult;
        }
        invitation = invitations.get(0);

        //如果是第一次请求验证码,需要更新invitation表中验证时间和验证次数
        //如果时间相较于前一天第一次验证时间过了一天,将第一次验证时间更新为现在的时间,验证次数设为0;
        long oneDay = 24 * 60 * 60 * 1000;
        if (invitation.getInvitationFirstCaptchaTime() == null || invitation.getInvitationFirstCaptchaTime().getTime() + oneDay < nowDate.getTime()) {
            invitation.setInvitationFirstCaptchaTime(nowDate);
            invitation.setInvitationCaptchaCount(0);
            invitationMapper.updateByPrimaryKey(invitation);
        }

        /*验证码获取次数*/
        int usedCount = invitation.getInvitationCaptchaCount() == null ? 0 : invitation.getInvitationCaptchaCount();
        int remainCount = TOTALCOUNT - usedCount;
        if (remainCount <= 0) {
            map.put("remainCount", remainCount + "");
            captchaResult.setData(map);
            captchaResult.setCode(EXCEEDING);
            captchaResult.setMsg("今天获取验证码次数已经达到上限");
            return captchaResult;
        }

        //确认了邀请码的正确性,有效性,和绑定邮箱一致,当天验证次数没有超过上限后,我们去查找邮件,查找条件是收到后不超过一个小时,未读,按收到时间降序排序
        Example captchaExample = new Example(Captcha.class);
        Example.Criteria criteria1 = captchaExample.createCriteria();
        criteria1.andEqualTo("captchaTo", accountEmail);
        criteria1.andLike("captchaFrom", "%@baidu.com");
        criteria1.andEqualTo("captchaSubject", "百度帐号--登录保护验证"); //防止有人修改密码
        criteria1.andEqualTo("captchaRead", 0);
        captchaExample.orderBy("captchaReceiveTime").desc();
        List<Captcha> captchas = captchaMapper.selectByExample(captchaExample);

        //邮箱表可能还没有对应邮箱的邮件,也有可能有很多封,当有很多封的时候,我们把最新的一封返回
        if (captchas.size() == 0) {
            map.put("remainCount", remainCount + "");
            captchaResult.setData(map);
            captchaResult.setCode(NOTRECEIVE);
            captchaResult.setMsg("验证码还未收到");
            return captchaResult;
        }
        captcha = captchas.get(0);

        //把验证码表中对应的captchaRead设为已读;
        captcha.setCaptchaRead(1);
        captchaMapper.updateByPrimaryKey(captcha);

        long oneHourAgo = nowDate.getTime() - 60 * 60 * 1000;
        if (captcha.getCaptchaReceiveTime().getTime() < oneHourAgo) {
            map.put("remainCount", remainCount + "");
            captchaResult.setData(map);
            captchaResult.setCode(NOTRECEIVE); // 其实这里是验证码已经过期, 但是考虑到实际逻辑, 修改为未收到
            captchaResult.setMsg("验证码还未收到");
            return captchaResult;
        }

        //成功的时候添加数据
        map.put("captchaCode", captcha.getCaptchaCode());

        //成功的时候,验证次数加1
        invitation.setInvitationCaptchaCount(usedCount + 1);
        invitationMapper.updateByPrimaryKey(invitation);

        /*验证码剩余可获取次数*/
        remainCount--;
        map.put("remainCount", remainCount + "");
        captchaResult.setData(map);
        return captchaResult;
    }

    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Invitation.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("invitation_create_time desc");
        if (searchMap != null) {
            if (searchMap.get(invitationId) != null && !"".equals(searchMap.get(invitationId))) {
                criteria.andEqualTo(invitationId, searchMap.get(invitationId));
            }
            if (searchMap.get(invitationCode) != null && !"".equals(searchMap.get(invitationCode))) {
                criteria.andEqualTo(invitationCode, searchMap.get(invitationCode));
            }
            if (searchMap.get(invitationLifetime) != null && !"".equals(searchMap.get(invitationLifetime))) {
                criteria.andEqualTo(invitationLifetime, searchMap.get(invitationLifetime));
            }
            if (searchMap.get(invitationEmail) != null && !"".equals(searchMap.get(invitationEmail))) {
                criteria.andEqualTo(invitationEmail, searchMap.get(invitationEmail));
            }
            if (searchMap.get(invitationCaptchaCount) != null && !"".equals(searchMap.get(invitationCaptchaCount))) {
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
        map.put("accountEmail", accountEmail);
        map.put("accountPassword", accountPassword);
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
