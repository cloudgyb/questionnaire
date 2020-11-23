package com.gyb.questionnaire.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

/**
 * 调查问卷答卷问题答案实体
 *
 * @author gengyb
 * 2020/11/21
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Paper {
    private String id;
    private String questionnaireId;
    private Date submitTime;
    //答卷耗时，单位秒
    private int elapsedTime;
    private int source;
    private String ip;
    private String address;
}
