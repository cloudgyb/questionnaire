package com.gyb.questionnaire.service;

import com.gyb.questionnaire.config.constans.UserStatusEnum;
import com.gyb.questionnaire.controller.ResponseResult;
import com.gyb.questionnaire.controller.form.SignupForm;
import com.gyb.questionnaire.dao.UserDao;
import com.gyb.questionnaire.entity.User;
import com.gyb.questionnaire.util.EncryptUtil;
import com.gyb.questionnaire.util.RandomUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 注册服务
 *
 * @author geng
 * 2020/11/5
 */

@Service
public class SignupService {
    private final UserDao userDao;

    public SignupService(UserDao userDao) {
        this.userDao = userDao;
    }

    public ResponseResult signup(SignupForm form){
        final int i = userDao.countByUsername(form.getUsername());
        if(i>0){
            return ResponseResult.error("用户名已被占用，请换一个吧！",null);
        }
        final User user = new User();
        user.setUsername(form.getUsername());
        final String password = form.getPassword();
        final String passSalt = RandomUtil.randomNumber(5);
        final String encryptPass = EncryptUtil.encryptPassword(password, passSalt);
        user.setPassword(encryptPass);
        user.setPasswordSalt(passSalt);
        user.setIsVip(0);
        user.setStatus(UserStatusEnum.NORMAL.getStatus());
        user.setCreateDate(new Date());
        userDao.addUser(user);
        return ResponseResult.ok("注册成功！",null);
    }
}
