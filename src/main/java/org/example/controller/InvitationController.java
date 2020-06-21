package org.example.controller;

import org.example.entity.PageResult;
import org.example.entity.Result;
import org.example.pojo.Invitation;
import org.example.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/invitation")
public class InvitationController {
    @Autowired
    private InvitationService invitationService;

    @GetMapping("/selectAll")
    @ResponseBody
    public List<Invitation> selectAll(){
        return invitationService.selectAll();
    }

    @GetMapping("/selectById")
    @ResponseBody
    public Invitation selectById(String id) {
        return invitationService.selectById(id);
    }

    /*分页*/
    @GetMapping("/selectPage")
    @ResponseBody
    public PageResult<Invitation> selectPage(int page, int size) {
        return invitationService.selectPage(page,size);
    }

    /*分页加条件*/
    @PostMapping("/selectPage")
    @ResponseBody
    public PageResult<Invitation> selectPage(@RequestBody Map<String,Object> searchmap, int page, int size) {
        return invitationService.selectPage(searchmap,page,size);
    }

    @PostMapping("/selectList")
    @ResponseBody
    public List<Invitation> selectList(@RequestBody Map<String, Object> searchmap){
        return invitationService.selectList(searchmap);
    }

    @PostMapping("/insert")
    @ResponseBody
    public Result insert(@RequestBody Invitation invitation){
        invitationService.insert(invitation);
        return new Result();
    }

    @PostMapping("/update")
    @ResponseBody
    public Result update(@RequestBody Invitation invitation) {
        invitationService.update(invitation);
        return new Result();
    }

    @GetMapping("/delete")
    @ResponseBody
    public Result delete(String id) {
        invitationService.delete(id);
        return new Result();
    }


    @RequestMapping("/checkInvitation")
    @ResponseBody
    public String checkInvitation(String invitationCode) {
        String msg = invitationService.checkInvitation(invitationCode);
        System.out.println(msg);
        return msg;
    }
}
