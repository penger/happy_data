//package com.gp.quantificat.conf;
//
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.gp.quantificat.service")
////@ComponentScan(basePackages = "")
//public class NewRestClientConfig extends ElasticsearchConfiguration {
//    @Override
//    public ClientConfiguration clientConfiguration() {
//
//        return ClientConfiguration.builder()
//                .connectedTo("192.168.1.123:9200")
//                .withBasicAuth("elastic","elastic")
//                .build();
//
//    }
//
//}
