package org.example.dao;

import org.example.pojo.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;


public interface CaptchaMapper extends Mapper<Captcha> {

}
