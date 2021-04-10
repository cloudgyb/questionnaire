package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.QuestionnaireQuestionOption;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 调查问卷问题选项dao
 *
 * @author geng
 * 2020/11/9
 */
@Repository
@Mapper
public interface QuestionnaireQuestionOptionDao {
    @Delete("delete from questionnaire_question_option where id=#{id}")
    int delete(Long id);

    @Delete("delete from questionnaire_question_option where question_id=#{questionId}")
    int deleteByQuestionId(Long questionId);

    @Select("select id," +
            "question_id as questionId," +
            "option_text as optionText," +
            "option_order as optionOrder " +
            "from questionnaire_question_option where question_id=#{questionId}")
    List<QuestionnaireQuestionOption> findByQuestionId(Long questionId);

    @Insert("insert into questionnaire_question_option(id,question_id,option_text,option_order) " +
            "value(#{id},#{questionId},#{optionText},#{optionOrder})")
    @Options(keyProperty = "id",useGeneratedKeys = true)
    int add(QuestionnaireQuestionOption questionOption);

    @Update("update questionnaire_question_option " +
            "set question_id=#{questionId},option_text=#{optionText}," +
            "option_order=#{optionOrder} " +
            "where id=#{id}")
    int update(QuestionnaireQuestionOption qo);
}
