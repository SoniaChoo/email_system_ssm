package org.example.entity;

import java.io.Serializable;

public class Result implements Serializable {
    private Integer code;//0代表成功,1代表失败
    private String message;

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /*无参构造是为了让其有一个默认值*/
    public Result() {
        this.code=0;
        this.message="success";
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
