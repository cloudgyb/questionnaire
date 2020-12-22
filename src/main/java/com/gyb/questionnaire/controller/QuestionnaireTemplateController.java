package com.gyb.questionnaire.controller;

import com.gyb.questionnaire.config.RequiredLogin;
import com.gyb.questionnaire.dto.QuestionnaireTemplateDTO;
import com.gyb.questionnaire.entity.ESTemplate;
import com.gyb.questionnaire.entity.Template;
import com.gyb.questionnaire.entity.QuestionnaireType;
import com.gyb.questionnaire.service.TemplateSearchService;
import com.gyb.questionnaire.service.TemplateService;
import com.gyb.questionnaire.service.QuestionnaireTypeService;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author geng
 * 2020/11/8
 */
@Controller
public class QuestionnaireTemplateController {
    private final QuestionnaireTypeService templateTypeService;
    private final TemplateService templateService;
    private final TemplateSearchService templateSearchService;

    public QuestionnaireTemplateController(QuestionnaireTypeService templateTypeService,
                                           TemplateService templateService,
                                           TemplateSearchService templateSearchService) {
        this.templateTypeService = templateTypeService;
        this.templateService = templateService;
        this.templateSearchService = templateSearchService;
    }

    @GetMapping("/template")
    public String questionnaireTemplate(Model model) {
        final List<QuestionnaireType> all = templateTypeService.getAll();
        model.addAttribute("categories", all);
        return "questionnaire_template";
    }

    /**
     * 查看问卷模板列表
     * @param typeId 问卷分类id
     */
    @GetMapping("/template/list/{typeId}")
    public String questionnaireTemplate(@PathVariable int typeId, Model model) {
        final List<Template> templates = templateService.getTypeId(typeId);
        final QuestionnaireType type = templateTypeService.getById(typeId);
        model.addAttribute("templates", templates);
        model.addAttribute("type", type);
        return "questionnaire_template_list";
    }
    @RequiredLogin
    @GetMapping("/template/preview/{templateId}")
    public String previewQuestionnaireTemplate(@PathVariable long templateId, Model model){
        QuestionnaireTemplateDTO templateDetail = templateService.getTemplateDetail(templateId);
        model.addAttribute("td",templateDetail);
        return "questionnaire_template_view";
    }

    @GetMapping("/template/search")
    public String search(String kw,Model m){
        final SearchHits<ESTemplate> search = templateSearchService.search(kw);
        m.addAttribute("res",search);
        return "questionnaire_template_search";
    }

    @GetMapping("/template/search/json")
    @ResponseBody
    public SearchHits<ESTemplate> search(String kw){
       return templateSearchService.search(kw);
    }

}
