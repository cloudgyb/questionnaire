package com.gyb.questionnaire.service;

import com.gyb.questionnaire.controller.ResponseResult;
import com.gyb.questionnaire.controller.form.UpdateUserForm;
import com.gyb.questionnaire.dao.UserDao;
import com.gyb.questionnaire.entity.User;
import com.gyb.questionnaire.util.EncryptUtil;
import com.gyb.questionnaire.util.HttpServletUtil;
import com.gyb.questionnaire.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.gyb.questionnaire.config.GlobalConstant.SESSION_KEY_CURR_USER;
import static com.gyb.questionnaire.config.GlobalConstant.SESSION_KEY_EMAIL_CODE;

/**
 * 当前登录用户Service
 *
 * @author geng
 * 2020/11/24
 */
@Slf4j
@Service
public class LoginUserService {
    private final UserDao userDao;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String mailFrom;

    public LoginUserService(UserDao userDao,JavaMailSender mailSender) {
        this.userDao = userDao;
        this.mailSender = mailSender;
    }

    public static User getLoginUser() {
        return (User) (HttpServletUtil.getSession().getAttribute(SESSION_KEY_CURR_USER));
    }

    @Transactional
    public void modifyUserInfo(UpdateUserForm form){
        final User loginUser = getLoginUser();
        final User user = userDao.find(loginUser.getId());
        user.setAge(form.getAge());
        user.setSex(form.getSex());
        user.setRealName(form.getRealName());
        final int res = userDao.update(user);
        updateLoginUserInfo(user);
        if(res != 1)
            log.error("更新用户信息失败！");
    }

    private void updateLoginUserInfo(User user) {
        HttpServletUtil.getSession().setAttribute(SESSION_KEY_CURR_USER,user);
    }

    @Transactional
    public ResponseResult modifyPassword(String oldPassword, String newPassword) {
        final User loginUser = getLoginUser();
        final User user = userDao.find(loginUser.getId());
        final String passwordSalt = user.getPasswordSalt();
        final String oldPass = EncryptUtil.encryptPassword(oldPassword, passwordSalt);
        assert oldPass != null;
        if(!oldPass.equals(user.getPassword())){
            return ResponseResult.error("原密码错误！",null);
        }
        final String newPass = EncryptUtil.encryptPassword(newPassword, passwordSalt);
        if(newPass != null && newPass.equals(oldPass)){
            return ResponseResult.error("新密码不能与原密码相同！",null);
        }
        userDao.updatePassword(user.getId(),newPass);
        return ResponseResult.ok();
    }

    @Transactional
    public ResponseResult bindPhone(String phone) {
        final User loginUser = getLoginUser();
        final int n = userDao.updatePhone(loginUser.getId(), phone);
        if(n <= 0){
            log.error("用户绑定手机号失败："+phone);
            return ResponseResult.error("绑定失败！",null);
        }
        loginUser.setPhone(phone);
        updateLoginUserInfo(loginUser);
        return ResponseResult.ok();
    }

    @Transactional
    public ResponseResult unbindPhone() {
        final User loginUser = getLoginUser();
        final int n = userDao.updatePhone(loginUser.getId(), "");
        if(n <= 0){
            log.error("用户解绑手机号失败");
            return ResponseResult.error("解绑失败！",null);
        }
        loginUser.setPhone("");
        updateLoginUserInfo(loginUser);
        return ResponseResult.ok();
    }

    @Transactional
    public ResponseResult bindEmail(String email) {
        final User loginUser = getLoginUser();
        final int n = userDao.updateEmail(loginUser.getId(), email);
        if(n <= 0){
            log.error("用户绑定邮箱失败"+email);
            return ResponseResult.error("绑定失败！",null);
        }
        loginUser.setEmail(email);
        updateLoginUserInfo(loginUser);
        return ResponseResult.ok();
    }

    @Transactional
    public ResponseResult unbindEmail() {
        final User loginUser = getLoginUser();
        final int n = userDao.updateEmail(loginUser.getId(), "");
        if(n <= 0){
            log.error("用户解绑邮箱失败");
            return ResponseResult.error("解绑失败！",null);
        }
        loginUser.setEmail("");
        updateLoginUserInfo(loginUser);
        return ResponseResult.ok();
    }

    public ResponseResult sendEmailCode(String email) {
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(mailFrom);
            messageHelper.setTo(email);
            messageHelper.setSubject("问卷Online-验证码");
            String code = RandomUtil.randomNumber(4);
            HttpServletUtil.getSession().setAttribute(SESSION_KEY_EMAIL_CODE,code);
            String html = "您的验证码为：<h3>"+code+"</h3>，5分钟内有效，请尽快使用！<br><br>"
                    +"如非本人操作，请勿略该邮件！"
                    +"<br><br><hr>"
                    +"问卷Online";
            messageHelper.setText(html,true);
            mailSender.send(mimeMessage);
            log.info("邮件已发送！");
            return ResponseResult.ok();
        }catch (MessagingException e){
            log.error("邮件发送失败！",e);
        }
        return ResponseResult.error("验证码发送失败,请稍后重试！",null);
    }
}
