package com.gyb.questionnaire.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 调查问卷实体
 *
 * @author gengyb
 * 2020/11/08
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Questionnaire {
  private Long id;
  private String name;
  private String greeting;
  private Date createDate;
  private Date publishDate;
  private long userId;
  private int typeId;
  private int questionCount;
  private long templateId;
  private int invokeCount;
  private int status;//0设计中，1已经发布，2已经结束
  private int paperCount; //已提交答卷计数

}
