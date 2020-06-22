package org.example.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="captcha")
public class Captcha implements Serializable {
    @Id
    @JSONField(name="captchaId")
    private String captchaId;

    @JSONField(name="from")
    private String captchaFrom;

    @JSONField(name="to")
    private String captchaTo;

    @JSONField(name="subject")
    private String captchaSubject;

    @JSONField(name="text")
    private String captchaContent;

    private String captchaCode;

    @JSONField(name="html")
    private String captchaHtml;

    private Integer captchaRead;//1表示已经读

    @JSONField(name="date",format="E, dd MMM yyyy HH:mm:ss Z")
    private Date captchaSendTime;

    private Date captchaReceiveTime;
}
