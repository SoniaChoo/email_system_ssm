package org.example.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.example.dao.CaptchaMapper;
import org.example.entity.PageResult;
import org.example.pojo.Captcha;
import org.example.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.*;

@Service
public class CaptchaServiceImpl implements CaptchaService{
    static Logger logger = Logger.getLogger(AccountServiceImpl.class);

    @Autowired
    private CaptchaMapper captchaMapper;

    public List<Captcha> selectAll() {
        return captchaMapper.selectAll();
    }

    public Captcha selectById(String id) {
        return captchaMapper.selectByPrimaryKey(id);
    }

    public List<Captcha> selectList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return captchaMapper.selectByExample(example);
    }

    /*分页*/
    public PageResult<Captcha> selectPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<Captcha> pageResult = (Page<Captcha>) captchaMapper.selectAll();
        return new PageResult<Captcha>(pageResult.getResult(), pageResult.getTotal());
    }

    /*分页+条件*/
    public PageResult<Captcha> selectPage(Map<String, Object> searchMap, int page, int size) {
        Example example = createExample(searchMap);
        PageHelper.startPage(page, size);
        Page<Captcha> pageResult = (Page<Captcha>) captchaMapper.selectByExample(example);
        return new PageResult<Captcha>(pageResult.getResult(), pageResult.getTotal());
    }

    public void insert(Captcha captcha) {
        if (captcha.getCaptchaId() == null) {
            captcha.setCaptchaId(UUID.randomUUID().toString());
        }
        if(captcha.getCaptchaReceiveTime()==null){
            captcha.setCaptchaReceiveTime(new Date());
        }
        captcha.setCaptchaRead(0);

        Map<String, String> map = JSON.parseObject(captcha.getCaptchaFrom(), Map.class);
        captcha.setCaptchaFrom(map.get("email"));

        JSONArray array = JSON.parseObject(captcha.getCaptchaTo(), JSONArray.class);
        if (array.size() == 0) {
            logger.error("captcha email no receiver. 验证码收件人为空。");
            throw new RuntimeException("验证码的收件人为空");
        }
        Map<String, String> stringMap = (Map<String, String>) array.get(0);
        captcha.setCaptchaTo(stringMap.get("email"));

        //通过正则表达式解析出html里面的验证码
        String captchCodeByRegex = Util.getCaptchCodeByRegex(captcha.getCaptchaHtml());
        captcha.setCaptchaCode(captchCodeByRegex);

        captchaMapper.insert(captcha);
    }

    public void update(Captcha captcha) {
        captchaMapper.updateByPrimaryKeySelective(captcha);
    }

    public void delete(String id) {
        captchaMapper.deleteByPrimaryKey(id);
    }

    public void log(HttpServletRequest request) {
        System.out.println("\nCaptchaServiceImpl#log()+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");

        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("Header\t\t\tValue");
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ":\t\t\t" + request.getHeader(headerName));
        }

        System.out.println("\nBody");
        try{
            BufferedReader br = request.getReader();
            String str = "";
            while((str = br.readLine()) != null){
                System.out.println(str);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("\nCaptchaServiceImpl#log()+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
        }
    }

    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Captcha.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            if (searchMap.get("captchaId") != null) {
                criteria.andEqualTo("captchaId", searchMap.get("captchaId"));
            }
            if (searchMap.get("captchaFrom") != null) {
                criteria.andEqualTo("captchaFrom", searchMap.get("captchaFrom"));
            }
            if (searchMap.get("captchaTo") != null) {
                criteria.andEqualTo("captchaTo", searchMap.get("captchaTo"));
            }
            if (searchMap.get("captchaSubject") != null) {
                criteria.andLike("captchaSubject", "%" + searchMap.get("captchaSubject") + "%");
            }
            if (searchMap.get("captchaContent") != null) {
                criteria.andLike("captchaContent", "%" + searchMap.get("captchaContent") + "%");
            }
            if (searchMap.get("captchaRead") != null) {
                criteria.andEqualTo("captchaRead", searchMap.get("captchaRead"));
            }
        }
        return example;
    }
}
