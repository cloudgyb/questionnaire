package com.gyb.questionnaire.dto;

import com.gyb.questionnaire.entity.Questionnaire;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 此类是一个包装类，包含了一个调查问卷的所有数据，包括问卷信息、问题和问题下的选项
 *
 * @author geng
 * 2020/11/13
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireDTO extends Questionnaire {
    private List<QuestionnaireQuestionDTO> questions;
}
