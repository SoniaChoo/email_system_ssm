package org.example.entity;

import java.util.Map;

public class InvitationResult {
    public static final int NOVALUE = -1;
    public static final int RIGHT = 0;
    public static final int WRONG = 1;
    public static final int EXPIRED = 2;
    public static final int TOTALCOUNT = 5;
    private Integer code;//0代表邀请码正确并且有效,1代表邀请码不正确,2代表邀请码失效,-1代表邀请码为空
    private String msg; //如果发生错误,代表错误的信息
    private Map<String,String> data; //代表返回的结果

    public InvitationResult() {
        this.code=RIGHT;
        this.msg="success";
    }

    public InvitationResult(Integer code, String msg, Map<String, String> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
