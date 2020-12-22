package com.gyb.questionnaire.service;

import com.gyb.questionnaire.dao.ESTemplateRepository;
import com.gyb.questionnaire.entity.ESTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

/**
 * 问卷调查模板搜索service
 *
 * @author geng
 * 2020/12/22
 */
@Service
public class TemplateSearchService {
    private final ESTemplateRepository esTemplateRepository;

    public TemplateSearchService(ESTemplateRepository esTemplateRepository) {
        this.esTemplateRepository = esTemplateRepository;
    }

    public SearchHits<ESTemplate> search(String keyword) {
        return esTemplateRepository.find(keyword);
    }
}
