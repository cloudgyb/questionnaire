package com.gyb.questionnaire.config;

import org.elasticsearch.client.RestHighLevelClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @author geng
 * 2020/12/22
 */
@Configuration
public class ESClientConfig extends AbstractElasticsearchConfiguration {
    @Value("${spring.elasticsearch.address:localhost:9200}")
    private String esAddress;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(esAddress)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}