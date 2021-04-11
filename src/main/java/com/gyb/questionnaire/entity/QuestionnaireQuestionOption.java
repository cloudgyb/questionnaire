package com.gyb.questionnaire.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 调查问卷问题选项实体
 *
 * @author gengyb
 * 2020/11/08
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireQuestionOption {

    private Long id;
    private Long questionId;
    private String optionText;
    private int optionOrder;
    private boolean isChecked;
    private int checkedCount;

}
