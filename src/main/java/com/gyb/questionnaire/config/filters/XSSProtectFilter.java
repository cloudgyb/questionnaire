package com.gyb.questionnaire.config.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * XSS 防护过滤器 <br>
 * 通过设置X-XSS-Protection和转义参数中html标签来达到简单XSS防护的作用
 *
 * @author cloudgyb
 * @since 2021/7/31 16:16
 */
public class XSSProtectFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final XSSProtectRequestWrapper xssProtectRequestWrapper = new XSSProtectRequestWrapper(request);
        response.addHeader("X-XSS-Protection", "1;mode=block");
        chain.doFilter(xssProtectRequestWrapper, response);
    }

}
