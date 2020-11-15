package com.gyb.questionnaire.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireType {
    private long id;
    private String typeName;
    private String typeDesc;

}
