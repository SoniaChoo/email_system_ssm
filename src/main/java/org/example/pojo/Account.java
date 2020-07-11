package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="account")
public class Account implements Serializable {
    @Id
    private String accountId;
    private String accountEmail;
    private String accountPassword;
    private Integer accountUsingCount;
}
