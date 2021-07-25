package com.gyb.questionnaire.entity;

import com.gyb.questionnaire.config.constans.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 用户实体
 *
 * @author gengyb
 * 2020/11/05
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;
    private String username;
    private String password;
    private String passwordSalt;
    private String realName;
    private String phone;
    private String email;
    private int age;
    private int sex;
    private int isVip;
    private Date createDate;
    /**
     * 用户状态
     * @see UserStatusEnum
     */
    private int status;

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }
}
