package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.Questionnaire;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 调查问卷dao
 *
 * @author geng
 * 2020/11/8
 */

@Mapper
@Repository
public interface QuestionnaireDao {
    @Insert("insert into questionnaire(" +
                "id,name,greeting,create_date," +
                "publish_date,user_id," +
                "type_id,question_count, " +
                "template_id,invoke_count,status" +
            ") value(" +
                "#{id},#{name},#{greeting},#{createDate},#{publishDate}," +
                "#{userId},#{typeId},#{questionCount},#{templateId}," +
                "#{invokeCount},#{status}" +
            ")")
    @Options(keyProperty = "id",useGeneratedKeys = true)
    void add(Questionnaire q);

    @Select("select id,name,greeting,create_date as createDate," +
            "publish_date as publishDate,user_id as userId," +
            "type_id as typeId,question_count as questionCount, " +
            "template_id as templateId,invoke_count as invokeCount,status " +
            "from questionnaire")
    List<Questionnaire> findAll();

    @Select("select id,name,greeting,create_date as createDate," +
            "publish_date as publishDate,user_id as userId," +
            "type_id as typeId,question_count as questionCount, " +
            "template_id as templateId,invoke_count as invokeCount,status " +
            "from questionnaire where user_id=#{userId} order by create_date desc")
    List<Questionnaire> findByUserId(long userId);

    @Select("select id,name,greeting,create_date as createDate," +
            "publish_date as publishDate,user_id as userId," +
            "type_id as typeId,question_count as questionCount, " +
            "template_id as templateId,invoke_count as invokeCount,status " +
            "from questionnaire where id=#{id}")
    Questionnaire find(long id);

    @Select("select id,name,greeting,create_date as createDate," +
            "publish_date as publishDate,user_id as userId," +
            "type_id as typeId,question_count as questionCount, " +
            "template_id as templateId,invoke_count as invokeCount,status " +
            "from questionnaire where id=#{id} and user_id=#{userId}")
    Questionnaire findByIdAndUserId(long id,long userId);

    @Update("update questionnaire " +
            "set name=#{name},greeting=#{greeting}," +
            "create_date=#{createDate}," +
            "publish_date=#{publishDate},user_id=#{userId}," +
            "type_id=#{typeId},question_count=#{questionCount}," +
            "template_id=#{templateId},invoke_count=#{invokeCount},status=#{status} " +
            "where id=#{id}")
    void update(Questionnaire q);

    @Delete("delete from questionnaire where id=#{id}")
    void delete(long id);
}
