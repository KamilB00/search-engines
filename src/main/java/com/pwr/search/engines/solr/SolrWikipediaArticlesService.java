package com.pwr.search.engines.solr;

import com.pwr.search.engines.EngineFacade;
import com.pwr.search.engines.Hit;
import com.pwr.search.engines.IndexArticlesResponse;
import com.pwr.search.engines.SearchResult;
import com.pwr.search.wikipedia.WikipediaArticle;
import lombok.RequiredArgsConstructor;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SolrWikipediaArticlesService implements EngineFacade {
    private final Logger log = LoggerFactory.getLogger(SolrWikipediaArticlesService.class);

    private final SolrClient solrClient;

    @Override
    public SearchResult search(String index, String query) {
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery(query);
            solrQuery.setRows(10);
            solrQuery.setFields("id", "title", "score");

            long startTime = System.currentTimeMillis();
            QueryResponse response = solrClient.query(index, solrQuery);
            long took = System.currentTimeMillis() - startTime;

            SolrDocumentList docs = response.getResults();
            List<Hit> hits = docs.stream().map(doc -> {

                int id = Integer.parseInt(doc.getFieldValue("id").toString());
                String title = (String) doc.getFieldValue("title");
                Double score = doc.getFieldValue("score") != null
                        ? ((Number) doc.getFieldValue("score")).doubleValue()
                        : null;

                return new SolrHit(id, score, title);
            }).collect(Collectors.toList());

            return new SolrSearchResult(docs.getNumFound(), hits, true, took);
        } catch (Exception e) {
            log.error("Solr search failed", e);
            return new SolrSearchResult(0, List.of(), false, 0);
        }
    }

    @Override
    public IndexArticlesResponse indexWikipediaArticles(List<WikipediaArticle> articles, String index) {
        return null;
    }

    @Override
    public void deleteIndex(String index) {

    }
}
