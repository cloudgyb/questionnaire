package com.gyb.questionnaire.dto;

import com.gyb.questionnaire.entity.TemplateQuestion;
import com.gyb.questionnaire.entity.TemplateQuestionOption;
import lombok.Data;

import java.util.List;

@Data
public class QuestionJsonParseWrap {
    private TemplateQuestion question;
    private List<TemplateQuestionOption> questionOptions;
}
