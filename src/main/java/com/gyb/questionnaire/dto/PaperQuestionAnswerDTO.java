package com.gyb.questionnaire.dto;

/**
 * @author geng
 * 2020/11/26
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 答卷问题和答案DTO
 *
 * @author geng
 * 2020/11/13
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaperQuestionAnswerDTO extends QuestionnaireQuestionDTO {
    private String answer;
}
