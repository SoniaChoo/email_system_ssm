package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class InvitationResult {
    private Integer code;// 对应的返回码
    private String msg; //如果发生错误,代表错误的信息
    private Map<String, String> data; //代表返回的结果
    private List<String> rows; // 代表实际生成的邀请码

    public InvitationResult() {
        this.code = 0;
        this.msg = "success";
    }

    @Override
    public String toString() {
        return "InvitationResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", rows=" + rows +
                '}';
    }
}
