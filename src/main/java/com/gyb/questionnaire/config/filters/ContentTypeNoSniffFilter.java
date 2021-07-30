package com.gyb.questionnaire.config.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 禁用浏览器客户端对Content-Type MIME类型嗅探。<br/>
 * 当web服务器对响应的请求未正确设置Content-Type响应头时，浏览器就会启动内容嗅探（猜测资源的类型），
 * 浏览器的内容嗅探技术可能会把不可执行的 MIME类型转变为可执行的MIME类型。<br/>
 * 通过对每个请求的响应设置‘<code>X-Content-Type-Options: nosniff</code>’头来禁用浏览器的嗅探行为。
 *
 * @author cloudgyb
 * @since 2021/7/25 17:24
 */
public class ContentTypeNoSniffFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("X-Content-Type-Options", "nosniff");
        filterChain.doFilter(request, response);
    }

}
