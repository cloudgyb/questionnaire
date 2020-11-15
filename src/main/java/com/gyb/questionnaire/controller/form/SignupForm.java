package com.gyb.questionnaire.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "验证码为空")
    private String code;
}
