package org.example.entity;

import java.util.Map;

public class CaptchaResult {
    public static final int RIGHT = 0;
    public static final int WRONG = 1;
    public static final int EXPIRED = 2;
    public static final int BINDING = 3;
    public static final int EXCEEDING = 4;
    public static final int NOTRECEIVE = 5;
    private Integer code;//0代表邀请码正确并且有效且去输入的邮箱一致,得到验证码,1代表邀请码不正确Wrong,2代表邀请码失效Expired,
    // 3代表邀请码绑定的邮箱和输入的邮箱不一致,4代表邀请码当天获取次数超额,5.0代表邀请码正确并且有效且去输入的邮箱一致,还没有收到验证码
    private String msg; //如果发生错误,代表错误的信息
    private Map<String,String> data; //代表返回的结果

    public CaptchaResult(Integer code, String msg, Map<String, String> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public CaptchaResult() {
        this.code=0;
        this.msg="success";
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
