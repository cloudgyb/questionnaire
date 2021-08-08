package com.gyb.questionnaire.controller.form;

import javax.validation.constraints.NotBlank;

import com.gyb.questionnaire.controller.validator.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * @author geng
 * 2020/11/5
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupForm {
    @NotBlank(message = "用户名不能为空")
    @Length(min = 1,max = 20,message = "用户名太长")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Password
    private String password;
    @NotBlank(message = "验证码为空")
    private String code;
}
