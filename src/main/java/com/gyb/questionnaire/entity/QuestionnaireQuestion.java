package com.gyb.questionnaire.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 调查问卷问题实体
 *
 * @author gengyb
 * 2020/11/08
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireQuestion {
  private Long id;
  private Long questionnaireId;
  private int questionType;
  private String questionTitle;
  private int questionOrder;
  private int questionNum;
  private String inputPlaceholder;
}
