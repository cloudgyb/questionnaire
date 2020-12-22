package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.ESTemplate;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Elasticsearch Repository for Questionnaire Template
 *
 * @author geng
 * 2020/12/22
 */
public interface ESTemplateRepository extends ElasticsearchRepository<ESTemplate, Integer> {

    @Highlight(
        fields = {
            @HighlightField(name = "name"),
            @HighlightField(name = "questions.questionTitle")
        }
    )
    @Query("{\"bool\":{\"should\":[{\"match_phrase\":{\"name\":{\"query\":\"?0\"}}},{\"nested\":{\"path\":\"questions\",\"query\":{\"bool\":{\"must\":[{\"match_phrase\":{\"questions.questionTitle\":\"?0\"}}]}}}}]}}")
    SearchHits<ESTemplate> find(String key);

}
