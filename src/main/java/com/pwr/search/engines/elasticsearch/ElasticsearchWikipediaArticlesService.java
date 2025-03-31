package com.pwr.search.engines.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.pwr.search.engines.wikipedia.WikipediaArticle;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class ElasticsearchWikipediaArticlesService {

    private static final String INDEX_NAME = "wikipedia_articles";

    private final Logger log = LoggerFactory.getLogger(ElasticsearchWikipediaArticlesService.class);

    private final ElasticsearchClient client;

    public List<Hit<WikipediaArticle>> search(String term) {
        try {
            SearchResponse<WikipediaArticle> response = client.search(s -> s
                            .index(INDEX_NAME)
                            .query(q -> q
                                    .match(t -> t
                                            .field("text")
                                            .query(term)
                                    )
                            ),
                    WikipediaArticle.class
            );
            return response.hits().hits();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    public BulkResponse bulkIndexArticles(List<WikipediaArticle> articles) throws IOException {
        List<BulkOperation> operations = articles.stream().map(article -> BulkOperation.of(b -> b.index(
                IndexOperation.of(i -> i
                        .index(INDEX_NAME)
                        .id(String.valueOf(article.getId()))
                        .document(article)
                ))
        )).toList();

        BulkRequest bulkRequest = BulkRequest.of(b -> b.operations(operations));
        return client.bulk(bulkRequest);
    }

    public void deleteIndex() {
        try {
            DeleteIndexResponse response = client.indices().delete(
                    DeleteIndexRequest.of(c -> c.index("wikipedia_articles"))
            );
            if (response.acknowledged()) {
                log.info("Indexed deleted successfully");
            }
        } catch (IOException e) {
            log.error("Could not delete index: {}", e.getMessage());
        }
    }


}
