package com.gyb.questionnaire.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gyb.questionnaire.config.CustomThreadFactory;
import com.gyb.questionnaire.dao.LoginLogDao;
import com.gyb.questionnaire.entity.LoginLog;
import com.gyb.questionnaire.entity.User;
import com.gyb.questionnaire.util.Client;
import com.gyb.questionnaire.util.ClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Timestamp;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 登录日志service
 *
 * @author geng
 * 2020/12/6
 */
@Slf4j
@Service
public class LoginLogService {
    private final LoginLogDao loginLogDao;
    private ThreadPoolExecutor threadPool;

    public LoginLogService(LoginLogDao loginLogDao) {
        this.loginLogDao = loginLogDao;
    }

    @PostConstruct
    public void init() {
        if (threadPool == null) {
            CustomThreadFactory customThreadFactory = new CustomThreadFactory("LoginLogThreadPool-thread");
            threadPool = new ThreadPoolExecutor(3, 5, 5 * 60,
                    TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000),customThreadFactory,
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
    }

    @PreDestroy
    public void destroy() {
        if (threadPool != null)
            threadPool.shutdown();
    }

    public void addLoginLog(long userId) {
        final Client clientInfo = ClientUtil.getClientInfo();
        threadPool.execute(() -> saveLoginLog(userId,clientInfo));
    }

    /**
     * 获取最近登录的size条日志
     * @param size 条数
     */
    public Page<LoginLog> getRecentLoginLog(int pageNum, int size){
        if(size <= 0)
            throw new IllegalArgumentException(String.format("size值为%s，不合法！", size));
        final User loginUser = LoginUserService.getLoginUser();
        Page<LoginLog> objects = PageHelper.startPage(pageNum, size);
        loginLogDao.findByUserId(loginUser.getId());
        return objects;
    }

    /**
     * 清空用户的日志
     */
    public void cleanUserLoginLog(){
        final User loginUser = LoginUserService.getLoginUser();
        final int n = loginLogDao.deleteByUserId(loginUser.getId());
        log.info(String.format("用户清空登录日志共%s条", n));
    }

    private void saveLoginLog(long userId,Client clientInfo) {
        final LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setClientName(clientInfo.getName());
        loginLog.setSystemName(clientInfo.getSystem());
        loginLog.setIp(clientInfo.getIp());
        String position = ClientUtil.getPositionByIp(clientInfo.getIp());
        loginLog.setPosition(position);
        loginLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
        final int n = loginLogDao.add(loginLog);
        if (n < 1) {
            log.error("添加登录日志失败！");
        }
    }


}
