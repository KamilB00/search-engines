package com.pwr.search.engines.elasticsearch;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.pwr.search.engines.wikipedia.WikipediaArticle;
import com.pwr.search.engines.wikipedia.WikipediaArticlesRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ElasticsearchWikipediaArticlesIndexerTest {

    @Autowired
    private ElasticsearchWikipediaArticlesService service;

    @Autowired
    private WikipediaArticlesRepository wikipediaArticlesRepository;

    private final Logger log = LoggerFactory.getLogger(ElasticsearchWikipediaArticlesIndexerTest.class);

    @Test
    void shouldDeleteIndex() {
        service.deleteIndex();
    }

    @Test
    void indexAllWikipediaArticles() {
        int batchSize = 1000;
        List<WikipediaArticle> allWikipediaArticles = wikipediaArticlesRepository.getArticles();
        List<List<WikipediaArticle>> batchedArticles = splitList(allWikipediaArticles, batchSize);
        log.info("Articles loaded");
        for (List<WikipediaArticle> batch : batchedArticles) {
            try {
                long start = System.currentTimeMillis();
                log.info("Started indexing batch...");
                BulkResponse response = service.bulkIndexArticles(batch);
                log.info("Batch processed");
                boolean isSuccessful = !response.errors();
                long end = System.currentTimeMillis();
                log.info("Indexing {} articles took {} milliseconds and result was {}", batch.size(), (end - start), isSuccessful ? "success" : "failure");
            } catch (Exception e) {
                log.error("Error while indexing: {}", e.getMessage(), e);
            }
        }
    }

    @Test
    void search() {
        List<Hit<WikipediaArticle>> hits = service.search("navy");

        for (Hit<WikipediaArticle> hit : hits) {
            WikipediaArticle product = hit.source();
            log.info("Hit score {} article {}", hit.score(), product.getTitle());
        }
    }

    public static <T> List<List<T>> splitList(List<T> originalList, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < originalList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, originalList.size());
            batches.add(new ArrayList<>(originalList.subList(i, end)));
        }
        return batches;
    }
}