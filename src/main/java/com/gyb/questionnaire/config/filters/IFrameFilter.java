package com.gyb.questionnaire.config.filters;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 防iframe嵌套，避免“点击劫持”攻击
 *
 * @author cloudgyb
 * @since 2021/7/25 17:24
 */
public class IFrameFilter extends OncePerRequestFilter {
    private final Set<String> ignoreUrl = new HashSet<>();

    public IFrameFilter(String ...ignoreUrlPatterns) {
        ignoreUrl.add("^/q/\\d{1,}$");
        ignoreUrl.add(".+\\.(js|css|png|jpg|gif|webp|woff2|ttf|eot|svg)$");
        ignoreUrl.addAll(Set.of(ignoreUrlPatterns));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        boolean isIgnore = false;
        for (String urlPattern : ignoreUrl) {
            if (requestURI.matches(urlPattern)) {
                isIgnore = true;
                break;
            }
        }
        if(!isIgnore)
            response.addHeader("X-Frame-Options", "sameorigin");
        filterChain.doFilter(request, response);
    }
}
