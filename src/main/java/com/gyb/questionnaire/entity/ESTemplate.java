package com.gyb.questionnaire.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * Elasticsearch 问卷模板搜索实体类
 *
 * @author geng
 * 2020/12/22
 */
@Data
@Document(indexName = "template")
public class ESTemplate {
    @Id
    private long id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;
    @Field(type = FieldType.Date, format = DateFormat.custom,pattern = "yyyy/MM/dd HH:mm:ss")
    private Date createDate;
    @Field(type = FieldType.Date, format = DateFormat.custom,pattern = "yyyy/MM/dd HH:mm:ss")
    private Date publishDate;
    private long authorId;
    private String authorName;
    private int typeId;
    private int questionCount;
    @Field(type = FieldType.Nested)
    private List<ESTemplateQuestion> questions;
}
