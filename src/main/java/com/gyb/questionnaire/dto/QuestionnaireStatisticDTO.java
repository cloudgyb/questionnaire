package com.gyb.questionnaire.dto;

import com.gyb.questionnaire.entity.Questionnaire;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 调查问卷统计数据DTO
 *
 * @author geng
 * 2020/11/27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireStatisticDTO {
    private Questionnaire questionnaire;
    private List<QuestionnaireQuestionStatisticDTO> questions;
}
