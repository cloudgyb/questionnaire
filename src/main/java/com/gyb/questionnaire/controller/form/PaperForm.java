package com.gyb.questionnaire.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 问卷答卷Form
 *
 * @author geng
 * 2020/11/23
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaperForm {
    private Long questionnaireId;
    private int elapsedTime;
    //通过哪种途径获取的答卷地址，0-网站链接，1-QQ，2-WX，3-QQ空间，4-微博,5-二维码扫码，6-其他
    private int source;
    private List<QuestionAnswer> resultList;
}
