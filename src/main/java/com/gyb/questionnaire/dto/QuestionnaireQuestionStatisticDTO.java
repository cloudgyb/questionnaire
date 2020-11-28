package com.gyb.questionnaire.dto;

/**
 * @author geng
 * 2020/11/26
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 答卷问题和统计答案DTO
 *
 * @author geng
 * 2020/11/27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireQuestionStatisticDTO extends QuestionnaireQuestionDTO {
    private List<String> answers;
}
