package com.gyb.questionnaire.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 问卷调查模板实体类
 *
 * @author gengyb
 * 2020/11/08
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Template {
    private long id;
    private String name;
    private java.sql.Timestamp createDate;
    private java.sql.Timestamp publishDate;
    private long authorId;
    private String authorName;
    private int typeId;
    private int questionCount;
    /**
     * 0未发布
     * 1已发布
     * 2以下线（等价于删除）
     */
    private int status;
}
