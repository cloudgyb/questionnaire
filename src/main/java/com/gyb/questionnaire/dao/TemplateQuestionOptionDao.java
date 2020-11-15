package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.TemplateQuestionOption;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 调查问卷模板问题选项dao
 *
 * @author geng
 * 2020/11/9
 */
@Repository
@Mapper
public interface TemplateQuestionOptionDao {
    @Delete("delete from template_question_option where id=#{id}")
    int delete(long id);

    @Delete("delete from template_question_option where id=#{id}")
    int deleteByQuestionId(long questionId);

    @Select("select id," +
            "question_id as questionId," +
            "option_text as optionText," +
            "option_order as optionOrder " +
            "from template_question_option where question_id=#{questionId}")
    List<TemplateQuestionOption> getByQuestionId(long questionId);

    @Insert("insert into template_question_option(question_id,option_text,option_order) " +
            "values(#{questionId},#{optionText},#{optionOrder})")
    int add(TemplateQuestionOption questionOption);

    @Update("update template_question_option " +
            "set question_id=#{questionId},option_text=#{optionText}," +
            "option_order=#{optionOrder}" +
            "where id=#{id}")
    int update(TemplateQuestionOption qo);
}
