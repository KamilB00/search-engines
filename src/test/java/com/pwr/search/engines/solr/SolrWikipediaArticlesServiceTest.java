package com.pwr.search.engines.solr;

import com.pwr.search.engines.Hit;
import com.pwr.search.engines.SearchResult;
import com.pwr.search.wikipedia.WikipediaArticlesRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SolrWikipediaArticlesServiceTest {

    private static final String WIKIPEDIA_SOLR_CORE = "wsu"; //CORE in solr terms

    @Autowired
    private SolrWikipediaArticlesService service;

    @Autowired
    private WikipediaArticlesRepository wikipediaArticlesRepository;

    private final Logger log = LoggerFactory.getLogger(SolrWikipediaArticlesServiceTest.class);

    @Test
    void search() {
        SearchResult result = service.search(WIKIPEDIA_SOLR_CORE, "text:\"us navy ship\"");

        for (Hit hit : result.hits()) {
            log.info("Hit score {} id: {} title: {}", hit.scoreBM25(), hit.articleId(), hit.title());
        }
    }
}
