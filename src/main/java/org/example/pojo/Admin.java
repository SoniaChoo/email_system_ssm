package org.example.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="admin")
public class Admin implements Serializable {
    @Id
    private Integer adminId;//id


    private String loginName;//用户名

    private String password;//密码

    private String status;//状态
}
