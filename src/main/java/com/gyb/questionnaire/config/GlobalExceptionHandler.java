package com.gyb.questionnaire.config;

import com.gyb.questionnaire.controller.ResponseResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * @author geng
 * 2020/11/5
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseResult bindException(BindException be){
        final List<FieldError> fieldErrors = be.getFieldErrors();
        String errMsg = "参数校验失败！";
        if(fieldErrors.size() > 0){
            final FieldError fieldError = fieldErrors.get(0);
            errMsg = fieldError.getDefaultMessage();
        }
        return ResponseResult.error(errMsg);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseResult missingServletRequestParameterException(MissingServletRequestParameterException e){
        final String parameterName = e.getParameterName();
        return ResponseResult.error("缺少参数"+parameterName);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseResult methodParameterValidateException(ConstraintViolationException e){
        final Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        final ConstraintViolation<?> next = violations.iterator().next();
        final String message = next.getMessage();
        return ResponseResult.error(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){
        return ResponseResult.error("服务器内部错误！");
    }

}
