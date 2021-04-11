package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.PaperAnswer;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 问卷答卷问题答案dao
 *
 * @author geng
 * 2020/11/21
 */
@Mapper
@Repository
public interface PaperAnswerDao {
    @Insert("insert into paper_answer(" +
            "id,paper_id,question_id,answer) " +
            "value(#{id},#{paperId},#{questionId},#{answer})")
    @Options(keyProperty = "id",useGeneratedKeys = true)
    int insert(PaperAnswer pa);

    @Update("update paper_answer set " +
            "paper_id=#{paperId}," +
            "question_id=#{questionId}," +
            "answer=#{answer} where id=#{id}")
    int update(PaperAnswer p);

    @Delete("delete from paper_answer where id=#{id}")
    int delete(long id);

    @Delete("delete from paper_answer where paper_id=#{paperId}")
    int deleteByPaperId(long paperId);

    @Delete("delete from paper_answer where question_id=#{questionId}")
    int deleteByQuestionId(long questionId);

    @Select("select id,paper_id as paperId," +
            "question_id as questionId," +
            "answer from paper_answer where id=#{id}")
    PaperAnswer find(long id);

    @Select("select id,paper_id as paperId," +
            "question_id as questionId," +
            "answer from paper_answer where paper_id=#{paperId}")
    List<PaperAnswer> findByPaperId(long paperId);

    @Select("select id,paper_id as paperId," +
            "question_id as questionId," +
            "answer from paper_answer where question_id=#{questionId}")
    List<PaperAnswer> findByQuestionId(long questionId);

    @Select("select answer from paper_answer where question_id=#{questionId} limit ${limit}")
    List<String> findAnswerByQuestionId(long questionId,int limit);

    @Select("select id,paper_id as paperId," +
            "question_id as questionId," +
            "answer from paper_answer where paper_id=#{paperId} and question_id=#{questionId}")
    PaperAnswer findByPaperIdAndQuestionId(long paperId, long questionId);

    @Select("select answer from paper_answer where paper_id=#{paperId} and question_id=#{questionId}")
    String findAnswerByPaperIdAndQuestionId(long paperId, long questionId);
}
