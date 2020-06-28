package org.example.controller;

import org.example.entity.PageResult;
import org.example.entity.Result;
import org.example.pojo.Captcha;
import org.example.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public PageResult<Captcha> selectPage(@RequestBody Map<String, Object> searchMap, int page, int size) {
        return captchaService.selectPage(searchMap, page, size);
    }

    @PostMapping("/selectList")
    @ResponseBody
    public List<Captcha> selectList(@RequestBody Map<String, Object> searchMap) {
        return captchaService.selectList(searchMap);
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

    @RequestMapping("/log")
    @ResponseBody
    public Result logCaptcha(HttpServletRequest request) {
        captchaService.log(request);
        return new Result();
    }
}
