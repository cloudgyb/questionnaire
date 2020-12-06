package com.gyb.questionnaire.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 登录日志实体
 *
 * @author gengyb
 * 2020/12/06
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginLog {
    private long id;
    private long userId;
    private String clientName;
    private String systemName;
    private String ip;
    private String position;
    private java.sql.Timestamp createTime;
}
