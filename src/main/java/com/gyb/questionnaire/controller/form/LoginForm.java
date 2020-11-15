package com.gyb.questionnaire.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author geng
 * 2020/11/5
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String code;
}
