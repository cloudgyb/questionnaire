package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.QuestionnaireQuestion;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 问卷问题dao
 *
 * @author geng
 * 2020/11/13
 */
@Repository
@Mapper
public interface QuestionnaireQuestionDao {
    @Select("select id, questionnaire_id as questionnaireId, question_type as questionType," +
            "question_order as questionOrder,question_num as questionNum," +
            "question_title as questionTitle, input_placeholder as inputPlaceholder " +
            "from questionnaire_question where questionnaire_id=#{questionnaireId}")
    List<QuestionnaireQuestion> findByQuestionnaireId(String questionnaireId);

    @Delete("delete from questionnaire_question where questionnaire_id=#{questionnaireId}")
    int deleteByQuestionnaireId(String questionnaireId); //根据问题id删除

    @Select("select id, questionnaire_id as questionnaireId, question_type as questionType," +
            "question_order as questionOrder,question_num as questionNum," +
            "question_title as questionTitle, input_placeholder as inputPlaceholder " +
            "from questionnaire_question where id=#{id}")
    QuestionnaireQuestion find(String id);

    @Select("select questionnaire_id from questionnaire_question where questionnaire_id=#{questionnaireId}")
    List<String> findIdByQuestionnaireId(String questionnaireId);

    @Update("update questionnaire_question set " +
            "questionnaire_id=#{questionnaireId},question_type=#{questionType}," +
            "question_order=#{questionOrder}," +
            "question_num=#{questionNum},question_title=#{questionTitle}," +
            "input_placeholder=#{inputPlaceholder} where id=#{id}")
    int update(QuestionnaireQuestion q);

    @Delete("delete from questionnaire_question where id=#{id}")
    void delete(String id);

    @Insert("insert into questionnaire_question(" +
            "id,questionnaire_id,question_type,question_order,question_num," +
            "question_title,input_placeholder) " +
            "value(#{id},#{questionnaireId},#{questionType},#{questionOrder}," +
            "#{questionNum},#{questionTitle},#{inputPlaceholder})")
    int add(QuestionnaireQuestion question);
}