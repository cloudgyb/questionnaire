package com.gyb.questionnaire.controller;

import com.gyb.questionnaire.controller.form.LoginForm;
import com.gyb.questionnaire.service.LoginLogoutService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

import static com.gyb.questionnaire.config.GlobalConstant.SESSION_KEY_LOGIN_ERR_COUNT;

/**
 * 登录登出控制器
 *
 * @author geng
 * 2020/11/4
 */

@Controller
public class LoginLogoutController {
    private final LoginLogoutService loginLogoutService;

    public LoginLogoutController(LoginLogoutService loginLogoutService) {
        this.loginLogoutService = loginLogoutService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/doLogin")
    @ResponseBody
    public ResponseResult login(@Validated LoginForm loginForm,HttpSession session){
        Object loginErrorCount = session.getAttribute(SESSION_KEY_LOGIN_ERR_COUNT);
        if(loginErrorCount != null){
            int count = (Integer) loginErrorCount;
            if(count > 3){ //登录失败三次，需要验证登录验证码
                String code = loginForm.getCode();
                if(code == null || code.equals("")){
                    return ResponseResult.error("请输入验证码！",count);
                }
                final String sessionCode = (String)session.getAttribute("_code");
                if(!sessionCode.equalsIgnoreCase(code)){
                    return ResponseResult.error("验证码错误！",count);
                }
            }
        }
        return loginLogoutService.login(loginForm);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
