package com.gyb.questionnaire.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 调查问卷答卷实体
 *
 * @author gengyb
 * 2020/11/21
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaperAnswer {

  private String id;
  private String paperId;
  private String questionId;
  private String answer;

}
