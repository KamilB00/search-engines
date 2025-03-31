package com.pwr.search.engines.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

import static java.util.Objects.requireNonNull;

@Configuration
public class ElasticsearchConfiguration {

    private final String username;

    private final String password;

    private final String host;

    private final int port;

    public ElasticsearchConfiguration(@Value("${elasticsearch.server.username}") String username,
                                      @Value("${elasticsearch.server.password}") String password,
                                      @Value("${elasticsearch.server.host}") String host,
                                      @Value("${elasticsearch.server.port}") int port
    ) {
        this.username = requireNonNull(username);
        this.password = requireNonNull(password);
        this.host = requireNonNull(host);
        this.port = port;
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() throws Exception {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials(username, password)
        );

        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((chain, authType) -> true)
                .build();

        RestClient restClient = RestClient
                .builder(new HttpHost(host, port, "https"))
                .setHttpClientConfigCallback(hc -> hc
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .setDefaultCredentialsProvider(credentialsProvider))
                .build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    @Bean
    ElasticsearchWikipediaArticlesService elasticsearchWikipediaArticlesService(ElasticsearchClient elasticsearchClient) {
        return new ElasticsearchWikipediaArticlesService(elasticsearchClient);
    }

}
