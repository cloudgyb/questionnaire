package com.gyb.questionnaire.config;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.gyb.questionnaire.config.GlobalConstant.LOGIN_URL;
import static com.gyb.questionnaire.config.GlobalConstant.SESSION_KEY_CURR_USER;

/**
 * 认证拦截器
 *
 * @author geng
 * 2020/11/12
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            RequiredLogin requiredLogin = method.getMethodAnnotation(RequiredLogin.class);
            if (requiredLogin != null) {
                if (request.getSession().getAttribute(SESSION_KEY_CURR_USER) == null) {
                    response.sendRedirect(LOGIN_URL + "?returnUrl=" +
                            request.getRequestURL().toString());
                    return false;
                }
            }
        }
        return true;
    }
}
