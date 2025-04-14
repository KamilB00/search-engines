package com.pwr.search.engines.solr;

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
public class SolrWikipediaArticlesServiceTest {

    private static final String WIKIPEDIA_SOLR_CORE = "wsu"; //CORE in solr terms

    record IndexingTimes(long update, long commit, long total) {

    }

    @Autowired
    private SolrWikipediaArticlesService service;

    @Autowired
    private WikipediaArticlesRepository wikipediaArticlesRepository;

    private final Logger log = LoggerFactory.getLogger(SolrWikipediaArticlesServiceTest.class);

    @Test
    void shouldDeleteIndex() {
        service.deleteIndex(WIKIPEDIA_SOLR_CORE);
    }

    @Test
    void indexAllWikipediaArticles() {
        int batchSize = 1000;
        List<WikipediaArticle> allWikipediaArticles = wikipediaArticlesRepository.getArticles();
        List<List<WikipediaArticle>> batchedArticles = splitList(allWikipediaArticles, batchSize);
        List<IndexingTimes> times = new ArrayList<>();
        log.info("Articles loaded");
        for (List<WikipediaArticle> batch : batchedArticles) {
            try {
                log.info("Started indexing batch...");
                IndexArticlesResponse response = service.indexWikipediaArticles(batch, WIKIPEDIA_SOLR_CORE);
                log.info("Batch processed");
                log.info("Indexing {} articles took {} milliseconds and result was {}", batch.size(), response.took(), response.isSuccessful() ? "success" : "failure");

                var solrResponse = (SolrIndexArticlesResponse) response;
                times.add(new IndexingTimes(solrResponse.updateTime(), solrResponse.commitTime(), solrResponse.took()));
            } catch (Exception e) {
                log.error("Error while indexing: {}", e.getMessage(), e);
            }
        }

        log.info("Measurements: ");
        for (var t : times) {
            log.info("{},{},{}", t.update(), t.commit(), t.total());
        }

    }

    @Test
    void search() {
        SearchResult result = service.search(WIKIPEDIA_SOLR_CORE, "text:\"us navy ship\"");

        for (Hit hit : result.hits()) {
            log.info("Hit score {} id: {} title: {}", hit.scoreBM25(), hit.articleId(), hit.title());
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
