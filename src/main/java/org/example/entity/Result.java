package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Result implements Serializable {
    public static final int NOVALUE = -1; // -1代表邀请码为空
    public static final int RIGHT = 0; // 0代表邀请码正确并且有效且去输入的邮箱一致,得到验证码
    public static final int WRONG = 1; // 1代表邀请码不正确Wrong
    public static final int EXPIRED = 2; // 2代表邀请码失效Expired
    public static final int BINDING = 3; // 3代表邀请码绑定的邮箱和输入的邮箱不一致
    public static final int EXCEEDING = 4; // 4代表邀请码当天获取次数超额
    public static final int NOTRECEIVE = 5; // 5代表邀请码正确并且有效且去输入的邮箱一致,还没有收到验证码
    public static final int OUTDATED = 6; // 6代表验证码已过期
    public static final int TOTALCOUNT = 5; // TOTALCOUNT 代表一天可以获取验证码的最大次数
    public static final int DUPLICATE = 8; // 8代表邀请码在数据库中有重复

    private Integer code;//0代表成功,1代表失败
    private String message;

    /*无参构造是为了让其有一个默认值*/
    public Result() {
        this.code = 0;
        this.message = "success";
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
