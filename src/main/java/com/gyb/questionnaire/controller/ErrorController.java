package com.gyb.questionnaire.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 覆盖Spring Boot自带的/error
 *
 * @author cloudgyb
 * 2021/7/25 16:47
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorController extends AbstractErrorController {

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String errorHtml(HttpServletRequest request,HttpServletResponse response) throws IOException {
        final HttpStatus status = getStatus(request);
        if(status.value() == HttpStatus.NOT_FOUND.value()){
            return "/error/404";
        }else {
            response.sendRedirect("/static/500.html");
        }
        return null;
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
