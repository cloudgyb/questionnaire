package com.gyb.questionnaire.config;

import com.gyb.questionnaire.config.filters.ContentTypeNoSniffFilter;
import com.gyb.questionnaire.config.filters.IFrameFilter;
import com.gyb.questionnaire.config.filters.XSSProtectFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Web安全配置类
 * 通过添加一些过滤器，对每个请求添加一些安全相关的响应头或其他操作以增加安全性
 *
 * @author cloudgyb
 * @since 2021/7/30 21:21
 */
@Configuration(proxyBeanMethods = false)
public class WebSecurityConfig {

    @Bean
    public ContentTypeNoSniffFilter contentTypeNoSniffFilter() {
        return new ContentTypeNoSniffFilter();
    }

    @Bean
    public IFrameFilter iFrameFilter() {
        return new IFrameFilter();
    }

    @Bean
    public XSSProtectFilter xssProtectFilter() {
        return new XSSProtectFilter();
    }

}
