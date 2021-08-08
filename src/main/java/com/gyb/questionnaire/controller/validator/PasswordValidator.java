package com.gyb.questionnaire.controller.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义密码校验器
 *
 * @author cloudgyb
 * @since 2021/8/8 13:32
 */
public class PasswordValidator implements ConstraintValidator<Password, CharSequence> {
    private Pattern pattern;

    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        final String regexp = constraintAnnotation.regexp();
        this.pattern = Pattern.compile(regexp);
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0)
            return false;
        final Matcher matcher = this.pattern.matcher(value);
        return matcher.matches();
    }
}
