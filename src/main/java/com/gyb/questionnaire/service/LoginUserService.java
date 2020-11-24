package com.gyb.questionnaire.service;

import com.gyb.questionnaire.entity.User;
import com.gyb.questionnaire.util.HttpServletUtil;

import static com.gyb.questionnaire.config.GlobalConstant.SESSION_KEY_CURR_USER;

/**
 * 当前登录用户Service
 *
 * @author geng
 * 2020/11/24
 */
public class LoginUserService {

    public static User getLoginUser(){
        return  (User) (HttpServletUtil.getSession().getAttribute(SESSION_KEY_CURR_USER));
    }
}
