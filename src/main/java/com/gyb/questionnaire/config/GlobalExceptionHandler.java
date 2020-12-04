package com.gyb.questionnaire.config;

import com.gyb.questionnaire.controller.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 *
 * @author geng
 * 2020/11/5
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseResult bindException(BindException be) {
        final List<FieldError> fieldErrors = be.getFieldErrors();
        String errMsg = "参数校验失败！";
        if (fieldErrors.size() > 0) {
            final FieldError fieldError = fieldErrors.get(0);
            errMsg = fieldError.getDefaultMessage();
        }
        log.error("参数校验失败："+errMsg,be);
        return ResponseResult.of(400,errMsg,null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseResult missingServletRequestParameterException(MissingServletRequestParameterException e) {
        final String parameterName = e.getParameterName();
        log.error("请求缺少参数："+parameterName,e);
        return ResponseResult.of(400,"缺少参数" + parameterName,null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseResult methodParameterValidateException(ConstraintViolationException e) {
        final Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        final ConstraintViolation<?> next = violations.iterator().next();
        final String message = next.getMessage();
        log.error("方法参数校验失败："+message,e);
        return ResponseResult.of(400,message,null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseResult requestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        log.error(e.getMessage(),e);
        return ResponseResult.of(405,"客户端错误，不支持"+e.getMethod()+"方法",null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e) {
        log.error("服务器内部错误",e);
        return ResponseResult.error("服务器内部错误！",null);
    }

}
