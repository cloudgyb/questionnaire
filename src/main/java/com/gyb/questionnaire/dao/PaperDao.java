package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.Paper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 问卷答卷dao
 *
 * @author geng
 * 2020/11/21
 */
@Mapper
@Repository
public interface PaperDao {
    @Insert("insert into paper(id,questionnaire_id,submit_time," +
            "elapsed_time,source,ip,address) " +
            "value(#{id},#{questionnaireId},#{submitTime}," +
            "#{elapsedTime},#{source},#{ip},#{address})")
    int insert(Paper p);

    @Update("update paper set " +
            "questionnaire_id=#{questionnaireId}," +
            "submit_time=#{submitTime}," +
            "elapsed_time=#{elapsedTime}," +
            "source=#{source}," +
            "ip=#{ip}," +
            "address=#{address} where id=#{id}")
    int update(Paper p);

    @Delete("delete from paper where id=#{id}")
    int delete(String id);

    @Delete("delete from paper where questionnaire_id=#{questionnaireId}")
    int deleteByQuestionnaireId(String questionnaireId);

    @Select("select id," +
            "questionnaire_id as questionnaireId," +
            "submit_time as submitTime," +
            "elapsed_time as elapsedTime," +
            "source," +
            "ip," +
            "address from paper where id=#{id}")
    Paper find(String id);

    @Select("select id," +
            "questionnaire_id as questionnaireId," +
            "submit_time as submitTime," +
            "elapsed_time as elapsedTime," +
            "source,ip,address " +
            "from paper where questionnaire_id=#{questionnaireId} " +
            "order by submit_time desc")
    List<Paper> findByQuestionnaireId(String questionnaireId);

    @Select("select id," +
            "questionnaire_id as questionnaireId," +
            "submit_time as submitTime," +
            "elapsed_time as elapsedTime," +
            "source,ip,address " +
            "from paper " +
            "where questionnaire_id=#{questionnaireId} and ip=#{ip}")
    Paper findByQuestionnaireIdAndIp(String questionnaireId, String ip);

    @Select("select count(questionnaire_id) "+
            "from paper " +
            "where questionnaire_id=#{questionnaireId}")
    int countByQuestionnaireId(String questionnaireId);

    @Select("select id "+
            "from paper " +
            "where questionnaire_id=#{questionnaireId}")
    List<String> findIdsByQuestionnaireId(String questionnaireId);
}
