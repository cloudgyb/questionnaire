package com.gyb.questionnaire.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 主页
 *
 * @author geng
 * 2020/11/5
 */
@Controller
public class IndexController {

    @GetMapping("")
    public String index() {
        return "index";
    }


}
