package com.gyb.questionnaire.controller.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.gyb.questionnaire.util.PatternMatchers;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义密码校验器注解
 *
 * @author cloudgyb
 * @since 2021/8/8 13:22
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(Password.List.class)
@Documented
@Constraint(validatedBy = {PasswordValidator.class})
public @interface Password {
    String message() default PatternMatchers.passwordRegxNotMatchMessage;
    /**
     * 自定义正则表达式，默认的密码正则表达式PatternMatchers.passwordRegx
     */
    String regexp() default PatternMatchers.passwordRegx;
    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Password[] value();
    }
}
