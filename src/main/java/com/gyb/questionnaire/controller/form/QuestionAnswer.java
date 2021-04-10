package com.gyb.questionnaire.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 答卷问题结果
 *
 * @author geng
 * 2020/11/23
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionAnswer {
    private long questionId;
    private int questionType;
    private String result;
}
