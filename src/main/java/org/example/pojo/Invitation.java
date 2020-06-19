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
@Table(name="invitation")
public class Invitation implements Serializable {
    @Id
    private String invitationId;
    private String invitationCode;
    private Integer invitationLifetime;
    private String invitationEmail;
    private Timestamp invitationActivateTime;
    private Integer invitationCaptchaCount;
    private Timestamp invitationFirstCaptchaTime;
}
