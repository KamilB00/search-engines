package com.pwr.search.engines.elasticsearch;

import co.elastic.clients.elasticsearch.indices.IndicesStatsResponse;
import co.elastic.clients.elasticsearch.nodes.NodesInfoResponse;
import com.pwr.search.engines.Hit;
import com.pwr.search.engines.IndexArticlesResponse;
import com.pwr.search.engines.SearchResult;
import com.pwr.search.wikipedia.WikipediaArticle;
import com.pwr.search.wikipedia.WikipediaArticlesRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class WikipediaArticlesElasticsearchServiceTest {

    private static final String WIKIPEDIA_ARTICLES_INDEX = "wikipedia_articles";

    @Autowired
    private WikipediaArticlesElasticsearchService service;

    @Autowired
    private WikipediaArticlesRepository wikipediaArticlesRepository;

    private final Logger log = LoggerFactory.getLogger(WikipediaArticlesElasticsearchServiceTest.class);

    @Test
    void shouldDeleteIndex() {
        service.deleteIndex("wikipedia_articles");
    }

    @Test
    void indexAllWikipediaArticles() {
        int batchSize = 1000;
        List<WikipediaArticle> allWikipediaArticles = wikipediaArticlesRepository.getArticles();
        List<WikipediaArticleWithCategory> allArticlesWithTestCategory = allWikipediaArticles.stream()
                .map(article -> new WikipediaArticleWithCategory(article, "test_category"))
                .toList();

        List<List<WikipediaArticleWithCategory>> batchedArticles = splitList(allArticlesWithTestCategory, batchSize);
        log.info("Articles loaded");
        for (List<WikipediaArticleWithCategory> batch : batchedArticles) {
            try {
                log.info("Started indexing batch...");
                IndexArticlesResponse response = service.indexWikipediaArticles(batch, WIKIPEDIA_ARTICLES_INDEX);
                log.info("Batch processed");
                log.info("Indexing {} articles took {} milliseconds and result was {}", batch.size(), response.took(), response.isSuccessful() ? "success" : "failure");
            } catch (Exception e) {
                log.error("Error while indexing: {}", e.getMessage(), e);
            }
        }
    }

    @Test
    void search() {
        SearchResult result = service.search(WIKIPEDIA_ARTICLES_INDEX, "us navy ship");
        log.info("Request {} total hits: {}, took: {} ms", result.isSuccessful() ? "success" : "failure", result.totalHits(), result.took());
        for (Hit hit : result.hits()) {
            log.info("Hit score {} id: {} title: {}", hit.scoreBM25(), hit.articleId(), hit.title());
        }
    }

    @Test
    void indexInfo() {
        IndicesStatsResponse response = service.indexStats(WIKIPEDIA_ARTICLES_INDEX).orElseThrow();
    }

    @Test
    void nodesInfo() {
        NodesInfoResponse nodesResponse = service.nodesInfo().orElseThrow();
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