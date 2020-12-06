package com.gyb.questionnaire.service;

import com.gyb.questionnaire.dao.LoginLogDao;
import com.gyb.questionnaire.entity.LoginLog;
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
            threadPool = new ThreadPoolExecutor(3, 5, 5 * 60,
                    TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000),
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

    private void saveLoginLog(long userId,Client clientInfo) {
        final LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setClientName(clientInfo.getName());
        loginLog.setSystemName(clientInfo.getSystem());
        loginLog.setIp(clientInfo.getIp());
        String position = ClientUtil.getPositionByIP(clientInfo.getIp());
        loginLog.setPosition(position);
        loginLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
        final int n = loginLogDao.add(loginLog);
        if (n < 1) {
            log.error("添加登录日志失败！");
        }
    }


}
