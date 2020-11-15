package com.gyb.questionnaire.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionParam {
    private Integer questionId;
    private Byte questionType;
    private Integer questionOrder;
    private Integer questionNum;
    private String questionTitle;
    private List<QuestionOptionParam> questionOptions;
    private String inputPlaceholder;

}
