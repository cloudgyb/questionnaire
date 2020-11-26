package com.gyb.questionnaire.dto;

import com.gyb.questionnaire.entity.Paper;
import com.gyb.questionnaire.entity.Questionnaire;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 答卷详情DTO
 *
 * @author geng
 * 2020/11/26
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaperDetailDTO {
    private Questionnaire q;
    private Paper paper;
    private List<PaperQuestionAnswerDTO> questions;
}
