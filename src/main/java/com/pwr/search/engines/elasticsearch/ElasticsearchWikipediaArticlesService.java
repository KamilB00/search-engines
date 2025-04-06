package com.pwr.search.engines.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndicesStatsResponse;
import co.elastic.clients.elasticsearch.nodes.NodesInfoResponse;
import com.pwr.search.engines.EngineFacade;
import com.pwr.search.engines.IndexArticlesResponse;
import com.pwr.search.engines.SearchResult;
import com.pwr.search.wikipedia.WikipediaArticle;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class ElasticsearchWikipediaArticlesService implements EngineFacade {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchWikipediaArticlesService.class);

    private final ElasticsearchClient client;

    @Override
    public SearchResult search(String index, String term) {
        try {
            SearchResponse<WikipediaArticle> response = client.search(s -> s
                            .index(index)
                            .query(q -> q
                                    .match(t -> t
                                            .field("text")
                                            .query(term)
                                    )
                            ),
                    WikipediaArticle.class
            );
            log.info("Search for term '{}' took: {} millis", term, response.took());
            return ElasticsearchSearchResult.success(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ElasticsearchSearchResult.failure();
        }
    }

    @Override
    public IndexArticlesResponse indexWikipediaArticles(List<WikipediaArticle> articles, String index) {
        List<BulkOperation> operations = articles.stream().map(article -> BulkOperation.of(b -> b.index(
                IndexOperation.of(i -> i
                        .index(index)
                        .id(String.valueOf(article.getId()))
                        .document(article)
                ))
        )).toList();

        BulkRequest bulkRequest = BulkRequest.of(b -> b.operations(operations));
        try {
            BulkResponse response = client.bulk(bulkRequest);
            return ElasticsearchIndexArticlesResponse.from(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ElasticsearchIndexArticlesResponse.failure();
        }
    }

    @Override
    public void deleteIndex(String index) {
        try {
            DeleteIndexResponse response = client.indices().delete(
                    DeleteIndexRequest.of(c -> c.index(index))
            );
            if (response.acknowledged()) {
                log.info("Indexed deleted successfully");
            }
        } catch (IOException e) {
            log.error("Could not delete index: {}", e.getMessage());
        }
    }

    public Optional<IndicesStatsResponse> indexStats(String index) {
        try {
            return Optional.of(client.indices().stats(s -> s.index(index)));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<NodesInfoResponse> nodesInfo() {
        try {
            return Optional.of(client.nodes().info());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }
}
