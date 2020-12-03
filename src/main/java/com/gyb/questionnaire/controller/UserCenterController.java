package com.gyb.questionnaire.controller;

import com.gyb.questionnaire.config.RequiredLogin;
import com.gyb.questionnaire.controller.form.UpdateUserForm;
import com.gyb.questionnaire.entity.User;
import com.gyb.questionnaire.service.LoginUserService;
import com.gyb.questionnaire.util.HttpServletUtil;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.gyb.questionnaire.config.GlobalConstant.SESSION_KEY_EMAIL_CODE;

/**
 * 用户个人中心
 *
 * @author geng
 * 2020/11/18
 */
@Validated
@Controller
public class UserCenterController {
    private final LoginUserService loginUserService;

    public UserCenterController(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @GetMapping("/user/profile")
    @RequiredLogin
    public String userProfile(Model m){
        final User loginUser = LoginUserService.getLoginUser();
        m.addAttribute("u",loginUser);
        return "user/profile";
    }

    @PostMapping("/user/modifyProfile")
    @RequiredLogin
    @ResponseBody
    public ResponseResult modifyProfile(UpdateUserForm form){
        loginUserService.modifyUserInfo(form);
        return ResponseResult.ok();
    }
    @PostMapping("/user/modifyPassword")
    @RequiredLogin
    @ResponseBody
    public ResponseResult modifyPassword(@NotBlank(message = "请输入原密码") String oldPassword,
                                         @NotBlank(message = "请输入新密码") String newPassword){
        return loginUserService.modifyPassword(oldPassword,newPassword);
    }

    @PostMapping("/user/bindPhone")
    @RequiredLogin
    @ResponseBody
    public ResponseResult bindPhone(@NotBlank(message = "手机号不能为空！")
                                    @Pattern(regexp = "^1([35789])\\d{9}$",message = "输入的手机号不合法！")
                                    @RequestParam String phone){
        return loginUserService.bindPhone(phone);
    }

    @PostMapping("/user/unbindPhone")
    @RequiredLogin
    @ResponseBody
    public ResponseResult unbindPhone(){
        return loginUserService.unbindPhone();
    }

    @PostMapping("/user/bindEmail")
    @RequiredLogin
    @ResponseBody
    public ResponseResult bindEmail(@NotBlank(message = "邮箱不能为空！")
                                    @Email(message = "输入的邮箱不合法！")
                                    @Length(max = 40,message = "邮箱长度过长，不超过四十个字符")
                                    @RequestParam String email,
                                    @NotBlank(message = "请输入验证码") String code){
        final String emailCode = (String) HttpServletUtil.getSession().getAttribute(SESSION_KEY_EMAIL_CODE);
        if(!code.equalsIgnoreCase(emailCode))
            return ResponseResult.error("验证码错误！",null);
        return loginUserService.bindEmail(email);
    }

    @PostMapping("/user/unbindEmail")
    @RequiredLogin
    @ResponseBody
    public ResponseResult unbindEmail(@NotBlank(message = "请输入验证码") String code){
        final String emailCode = (String) HttpServletUtil.getSession().getAttribute(SESSION_KEY_EMAIL_CODE);
        if(!code.equalsIgnoreCase(emailCode))
            return ResponseResult.error("验证码错误！",null);
        return loginUserService.unbindEmail();
    }

    @PostMapping("/user/sendEmailCode")
    @RequiredLogin
    @ResponseBody
    public ResponseResult sendEmailCode(@NotBlank(message = "请输入邮箱")
                                        @Email(message = "邮箱格式错误！") String email){
        return loginUserService.sendEmailCode(email);
    }

}
