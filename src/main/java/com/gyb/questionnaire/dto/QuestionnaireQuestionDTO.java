package com.gyb.questionnaire.dto;

import com.gyb.questionnaire.entity.QuestionnaireQuestion;
import com.gyb.questionnaire.entity.QuestionnaireQuestionOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author geng
 * 2020/11/13
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireQuestionDTO extends QuestionnaireQuestion {
    private List<QuestionnaireQuestionOption> options;
}
