package com.gyb.questionnaire.dto;

import com.gyb.questionnaire.entity.Template;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionnaireTemplateDTO extends Template {
    private List<TemplateQuestionDTO> questions;
}
