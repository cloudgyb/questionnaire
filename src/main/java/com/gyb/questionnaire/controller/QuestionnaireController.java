package com.gyb.questionnaire.controller;

import com.gyb.questionnaire.config.RequiredLogin;
import com.gyb.questionnaire.dto.QuestionnaireDTO;
import com.gyb.questionnaire.entity.Questionnaire;
import com.gyb.questionnaire.service.QuestionnaireService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 问卷
 *
 * @author geng
 * 2020/11/8
 */
@Controller
@RequestMapping("/questionnaire")
@Validated
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @GetMapping("/create")
    @RequiredLogin
    public String createQuestionnairePage() {
        return "questionnaire_create";
    }

    @PostMapping("/doCreate")
    @ResponseBody
    @RequiredLogin
    public ResponseResult createQuestionnaire(@NotBlank(message = "问卷名称不能为空") String name,
                                              @RequestParam(required = false) String greeting) {
        final String id = questionnaireService.add(name, greeting);
        return ResponseResult.ok(id);
    }

    /**
     * 进入问卷设计页面
     */
    @GetMapping("/design/{questionnaireId}")
    @RequiredLogin
    public String designQuestionnairePage(@PathVariable String questionnaireId, Model m) {
        final QuestionnaireDTO questionnaire = questionnaireService.getUserQuestionnaireDetail(questionnaireId);
        m.addAttribute("q",questionnaire);
        return "questionnaire_design";
    }

    /**
     * 保存问卷信息
     */
    @PostMapping("/design/saveQuestionnaire")
    @RequiredLogin
    @ResponseBody
    public ResponseResult saveQuestionnaire(@RequestBody QuestionnaireDTO dto){
        System.out.println(dto);
        return questionnaireService.saveQuestionnaire(dto);
    }

    @RequiredLogin
    @GetMapping("/list")
    public String userQuestionnaireList(Model m){
        List<Questionnaire> userQuestionnaireList = questionnaireService.getUserQuestionnaireList();
        m.addAttribute("list",userQuestionnaireList);
        return "questionnaire_list";
    }
}
