package com.gyb.questionnaire.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyb.questionnaire.config.RequiredLogin;
import com.gyb.questionnaire.dto.QuestionnaireStatisticDTO;
import com.gyb.questionnaire.service.QuestionnaireStatisticsService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 问卷调查结果的分析与统计相关
 *
 * @author geng
 * 2020/11/27
 */
@Controller
public class QuestionnaireStatisticsController {
    private final QuestionnaireStatisticsService statisticsService;

    public QuestionnaireStatisticsController(QuestionnaireStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/questionnaire/statistics/{questionnaireId}")
    @RequiredLogin
    public String analysisAndStatistics(@PathVariable Long questionnaireId,
                                        Model m){
        final QuestionnaireStatisticDTO dto = statisticsService.statistic(questionnaireId);
        m.addAttribute("dto",dto);
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String s = objectMapper.writeValueAsString(dto);
            m.addAttribute("json",s);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "questionnaire_statistics";
    }

}
