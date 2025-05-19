package com.pwr.search.engines.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Objects.requireNonNull;

@Configuration
public class ElasticsearchConfiguration {

    private final String username;

    private final String password;

    private final String host;

    private final int port;

    public ElasticsearchConfiguration(
            @Value("${elasticsearch.server.username}") String username,
            @Value("${elasticsearch.server.password}") String password,
            @Value("${elasticsearch.server.host}") String host,
            @Value("${elasticsearch.server.port}") int port
    ) {
        this.host = requireNonNull(host);
        this.port = port;
        this.username = requireNonNull(username);
        this.password = requireNonNull(password);
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClient restClient = RestClient
                .builder(new HttpHost(host, port, "http"))
                .build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

}
