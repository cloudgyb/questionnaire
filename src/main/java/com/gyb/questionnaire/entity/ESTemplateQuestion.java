package com.gyb.questionnaire.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author geng
 * 2020/12/22
 */
@Data
public class ESTemplateQuestion {
    private Long id;
    private long templateId;
    private int questionType;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String questionTitle;
    private int questionOrder;
    private int questionNum;
    private String inputPlaceholder;
}
