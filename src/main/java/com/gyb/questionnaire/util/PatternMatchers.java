package com.gyb.questionnaire.util;

import java.util.regex.Pattern;

/**
 * 正则匹配器常量类
 *
 * @author cloudgyb
 * @since 2021/8/8 12:06
 */
public final class PatternMatchers {

    //密码必须包含大写字母、小写字母、特殊符号和数字
    public static final String passwordRegx = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*]).{8,16}$";
    public static final String passwordRegxNotMatchMessage = "密码必须包含大小写字母、数字、特殊符号，且长度为8~16！";
    @SuppressWarnings("unused")
    public static final Pattern passwordPattern = Pattern.compile(passwordRegx);
}
