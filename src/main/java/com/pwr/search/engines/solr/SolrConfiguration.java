package com.pwr.search.engines.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpJdkSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Objects.requireNonNull;

@Configuration
public class SolrConfiguration {
    private final String username;

    private final String password;

    private final String host;

    private final int port;

    public SolrConfiguration(@Value("${solr.server.username}") String username,
                             @Value("${solr.server.password}") String password,
                             @Value("${solr.server.host}") String host,
                             @Value("${solr.server.port}") int port
    ) {
        this.username = requireNonNull(username);
        this.password = requireNonNull(password);
        this.host = requireNonNull(host);
        this.port = port;
    }

    @Bean
    public SolrClient solrClient() {
        String solrUrl = String.format("http://%s:%d/solr", host, port);

        return new HttpJdkSolrClient.Builder(solrUrl)
                .withBasicAuthCredentials(username, password)
                .build();
    }

    @Bean
    public SolrWikipediaArticlesService solrWikipediaArticlesService(SolrClient solrClient) {
        return new SolrWikipediaArticlesService(solrClient);
    }
}
