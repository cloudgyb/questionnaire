package com.gyb.questionnaire.dto;

import com.gyb.questionnaire.entity.TemplateQuestion;
import com.gyb.questionnaire.entity.TemplateQuestionOption;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateQuestionDTO extends TemplateQuestion {
    private List<TemplateQuestionOption> questionOptions;
}
