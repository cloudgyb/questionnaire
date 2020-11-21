package com.gyb.questionnaire.controller;

import com.gyb.questionnaire.config.RequiredLogin;
import com.gyb.questionnaire.dto.QuestionnaireDTO;
import com.gyb.questionnaire.entity.Questionnaire;
import com.gyb.questionnaire.service.QuestionnaireService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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

    @GetMapping("/createByTemplate")
    @RequiredLogin
    public String createQuestionnaireByTemplate(@RequestParam long templateId) {
        String id = questionnaireService.addByTemplate(templateId);
        if(id == null)
            id="templateerror";
        return String.format("redirect:/questionnaire/design/%s",id);
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
     * 预览问卷页面
     */
    @GetMapping("/preview/{questionnaireId}")
    @RequiredLogin
    public String previewQuestionnairePage(@PathVariable String questionnaireId, Model m) {
        final QuestionnaireDTO questionnaire = questionnaireService.getUserQuestionnaireDetail(questionnaireId);
        m.addAttribute("qa",questionnaire);
        return "questionnaire_view";
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

    /**
     * 发布问卷
     */
    @PostMapping("/design/publishQuestionnaire")
    @RequiredLogin
    @ResponseBody
    public ResponseResult publishQuestionnaire(@RequestParam String questionnaireId){
        return questionnaireService.publishQuestionnaire(questionnaireId);
    }
    /**
     * 停止问卷答卷收集，将问卷状态设置为结束，也就是问卷收集完成
     */
    @PostMapping("/stop")
    @RequiredLogin
    @ResponseBody
    public ResponseResult stopQuestionnaire(@RequestParam String questionnaireId){
        return questionnaireService.stopQuestionnaire(questionnaireId);
    }

    /**
     * 删除问题
     * @param questionId 问题id
     */
    @PostMapping("/design/delQuestion")
    @RequiredLogin
    @ResponseBody
    public ResponseResult deleteQuestion(@RequestParam String questionId){
        questionnaireService.deleteQuestion(questionId);
        return ResponseResult.ok();
    }

    /**
     * 删除问题选项
     * @param optionId 选项id
     */
    @PostMapping("/design/delOption")
    @RequiredLogin
    @ResponseBody
    public ResponseResult deleteQuestionOption(@RequestParam String optionId){
        questionnaireService.deleteQuestionOption(optionId);
        return ResponseResult.ok();
    }

    @RequiredLogin
    @GetMapping("/list")
    public String userQuestionnaireList(Model m){
        List<Questionnaire> userQuestionnaireList = questionnaireService.getUserQuestionnaireList();
        m.addAttribute("list",userQuestionnaireList);
        return "questionnaire_list";
    }

    /**
     * 删除问卷
     * @param questionnaireId 问卷id
     */
    @RequiredLogin
    @PostMapping("/delete")
    @ResponseBody
    public ResponseResult deleteQuestionnaire(@RequestParam String questionnaireId){
        return questionnaireService.delete(questionnaireId);
    }

    /**
     * 分享问卷
     * @param qId 问卷id
     */
    @RequiredLogin
    @GetMapping("/share")
    public String shareQuestionnaire(@RequestParam(required = false) String qId,Model m){
        if(!StringUtils.hasText(qId))
            return "questionnaire_share";
        Questionnaire questionnaire = questionnaireService.getUserQuestionnaire(qId);
        m.addAttribute("q",questionnaire);
        return "questionnaire_share";
    }

}
