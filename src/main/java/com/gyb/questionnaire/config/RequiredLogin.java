package com.gyb.questionnaire.config;

import java.lang.annotation.*;

/**
 * 要求用户必须登录才能访问的注解
 * 标注到controller上或者带有、@RequestMapping的方法上，被标注的资源只有登录才可访问
 *
 * @author geng
 * 2020/11/12
 */
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredLogin {
}
