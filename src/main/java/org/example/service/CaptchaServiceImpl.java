package org.example.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.example.Util;
import org.example.dao.CaptchaMapper;
import org.example.dao.InvitationMapper;
import org.example.entity.PageResult;
import org.example.pojo.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CaptchaServiceImpl implements CaptchaService{

    @Autowired
    private CaptchaMapper captchaMapper;

    public List<Captcha> selectAll() {
        return captchaMapper.selectAll();
    }

    public Captcha selectById(String id) {
        return captchaMapper.selectByPrimaryKey(id);
    }

    /*分页*/
    public PageResult<Captcha> selectPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Captcha> pageResult = (Page<Captcha>) captchaMapper.selectAll();
        return new PageResult<Captcha>(pageResult.getResult(),pageResult.getTotal());
    }

    /*分页+条件*/
    public PageResult<Captcha> selectPage(Map<String, Object> searchmap, int page, int size) {
        Example example = createExample(searchmap);
        PageHelper.startPage(page,size);
        Page<Captcha> pageResult = (Page<Captcha>) captchaMapper.selectByExample(example);
        return new PageResult<Captcha>(pageResult.getResult(),pageResult.getTotal());
    }

    public List<Captcha> selectList(Map<String, Object> searchmap) {
        Example example = createExample(searchmap);
        return captchaMapper.selectByExample(example);
    }

    public void insert(Captcha captcha) {
        if (captcha.getCaptchaId() == null) {
            captcha.setCaptchaId(UUID.randomUUID().toString());
        }
        if(captcha.getCaptchaReceiveTime()==null){
            captcha.setCaptchaReceiveTime(new Date());
        }

        captcha.setCaptchaRead(0);

        Map<String,String> map = JSON.parseObject(captcha.getCaptchaFrom(), Map.class);
        captcha.setCaptchaFrom(map.get("email"));

        JSONArray array = JSON.parseObject(captcha.getCaptchaTo(),JSONArray.class);
        if (array.size() == 0) {
           throw new RuntimeException("验证码的收件人为空");
        }

        //通过正则表达式解析出html里面的验证码
        String captchCodeByRegex = Util.getCaptchCodeByRegex(captcha.getCaptchaHtml());
        captcha.setCaptchaCode(captchCodeByRegex);

        Map<String, String> stringMap = (Map<String, String>) array.get(0);
        captcha.setCaptchaTo(stringMap.get("email"));

        captchaMapper.insert(captcha);
    }

    public void update(Captcha captcha) {
        captchaMapper.updateByPrimaryKeySelective(captcha);
    }

    public void delete(String id) {
        captchaMapper.deleteByPrimaryKey(id);
    }

    private Example createExample(Map<String,Object> searchmap) {
        Example example = new Example(Captcha.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchmap != null) {
            if (searchmap.get("captchaId") != null) {
                criteria.andEqualTo("captchaId", searchmap.get("captchaId"));
            }
            if (searchmap.get("captchaFrom") != null) {
                criteria.andEqualTo("captchaFrom",searchmap.get("captchaFrom"));
            }
            if (searchmap.get("captchaTo") != null) {
                criteria.andEqualTo("captchaTo",searchmap.get("captchaTo"));
            }
            if (searchmap.get("captchaSubject") != null) {
                criteria.andLike("captchaSubject","%"+searchmap.get("captchaSubject")+"%");
            }
            if (searchmap.get("captchaContent") != null) {
                criteria.andLike("captchaContent","%"+searchmap.get("captchaContent")+"%");
            }
            if(searchmap.get("captchaRead") != null) {
                criteria.andEqualTo("captchaRead",searchmap.get("captchaRead"));
            }
        }
        return example;
    }
}
