package com.gyb.questionnaire.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author geng
 * 2020/11/5
 */
public class HttpServletUtil {
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return requestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return requestAttributes.getResponse();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static boolean isAjaxRequest() {
        final HttpServletRequest request = getRequest();
        final String header = request.getHeader("x-requested-with");
        return "XMLHttpRequest".equalsIgnoreCase(header);
    }
}
