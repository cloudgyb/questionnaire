package com.gyb.questionnaire.controller;

import com.gyb.questionnaire.controller.form.SignupForm;
import com.gyb.questionnaire.service.SignupService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 用户注册控制器
 *
 * @author geng
 * 2020/11/4
 */
@Controller
public class SignupController {
    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }

    @PostMapping("/doSignup")
    @ResponseBody
    public ResponseResult doSignup(@Validated SignupForm form, HttpSession session){
        final String code = form.getCode();
        if(!code.equalsIgnoreCase((String) session.getAttribute("_code"))){
            return ResponseResult.error("验证码错误",null);
        }
        return signupService.signup(form);
    }
}
