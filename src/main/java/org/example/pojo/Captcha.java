package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="captcha")
public class Captcha implements Serializable {
    @Id
    private String captchaId;
    private String captchaFrom;
    private String captchaTo;
    private String captchaSubject;
    private String captchaContent;
    private Integer captchaRead;
    private Timestamp captchaCreateTime;
}
