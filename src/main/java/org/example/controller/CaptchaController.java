package org.example.controller;

import org.example.entity.PageResult;
import org.example.entity.Result;
import org.example.pojo.Captcha;
import org.example.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/selectAll")
    @ResponseBody
    public List<Captcha> selectAll(){
        return captchaService.selectAll();
    }

    @GetMapping("/selectById")
    @ResponseBody
    public Captcha selectById(String id) {
        return captchaService.selectById(id);
    }

    /*分页*/
    @GetMapping("/selectPage")
    @ResponseBody
    public PageResult<Captcha> selectPage(int page,int size) {
        return captchaService.selectPage(page,size);
    }

    /*分页加条件*/
    @PostMapping("/selectPage")
    @ResponseBody
    public PageResult<Captcha> selectPage(@RequestBody Map<String,Object> searchmap,int page,int size) {
        return captchaService.selectPage(searchmap,page,size);
    }

    @PostMapping("/selectList")
    @ResponseBody
    public List<Captcha> selectList(@RequestBody Map<String, Object> searchmap){
        return captchaService.selectList(searchmap);
    }

    @PostMapping("/insert")
    @ResponseBody
    public Result insert(@RequestBody Captcha captcha){
         captchaService.insert(captcha);
         return new Result();
    }

    @PostMapping("/update")
    @ResponseBody
    public Result update(@RequestBody Captcha captcha) {
        captchaService.update(captcha);
        return new Result();
    }

    @GetMapping("/delete")
    @ResponseBody
    public Result delete(String id) {
        captchaService.delete(id);
        return new Result();
    }

}
