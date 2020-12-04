package com.gyb.questionnaire.config;

import com.gyb.questionnaire.util.HttpServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.gyb.questionnaire.config.GlobalConstant.LOGIN_URL;
import static com.gyb.questionnaire.config.GlobalConstant.SESSION_KEY_CURR_USER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 认证拦截器
 *
 * @author geng
 * 2020/11/12
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            RequiredLogin requiredLogin = method.getMethodAnnotation(RequiredLogin.class);
            if (requiredLogin != null) {
                if (request.getSession().getAttribute(SESSION_KEY_CURR_USER) == null) {
                    if(HttpServletUtil.isAjaxRequest()) {
                        responseNoLoginJson(response);
                    }else {
                        String queryString = request.getQueryString();
                        queryString = (queryString != null && !"".equals(queryString)) ? "?" + queryString : "";
                        response.sendRedirect(LOGIN_URL + "?returnUrl=" +
                                request.getRequestURL().toString() + queryString);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private void responseNoLoginJson(HttpServletResponse response) {
        try {
            response.setContentType(APPLICATION_JSON_VALUE);
            final ServletOutputStream outputStream = response.getOutputStream();
            String json = "{\"code\":401," +
                            "\"msg\":\"未登录或登录已过期\"," +
                            "\"data\":\"\"}";
            outputStream.write(json.getBytes(UTF_8));
            outputStream.flush();
        }catch (IOException e){
            log.error("返回jsonIO异常",e);
        }
    }
}
