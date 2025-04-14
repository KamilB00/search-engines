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
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
    public IndexArticlesResponse indexWikipediaArticles(List<WikipediaArticle> articles, String index) throws RuntimeException {

        try {
            log.info("Loading data...");
            List<SolrInputDocument> docs = getSolrInputDocuments(articles);

            log.info("Indexing...");
            // Send all documents in a single batch
            UpdateResponse updateResponse = solrClient.add(index, docs);
            UpdateResponse commitResponse = solrClient.commit(index);

            // Measure Solr response time only
            long indexingTimeMs = updateResponse.getElapsedTime() + commitResponse.getElapsedTime();

            log.info("Indexed 1000, time: {}", indexingTimeMs);

            return new SolrIndexArticlesResponse(
                    indexingTimeMs,
                    true,
                    commitResponse.getElapsedTime(),
                    updateResponse.getElapsedTime()
            );

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static List<SolrInputDocument> getSolrInputDocuments(List<WikipediaArticle> articles) {
        List<SolrInputDocument> docs = new ArrayList<>();

        for (WikipediaArticle article : articles) {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", article.getId());
            doc.addField("title", article.getTitle());
            doc.addField("url", article.getUrl());
            doc.addField("text", article.getText());
            doc.addField("article_type", article.getArticle_type());
            docs.add(doc);
        }
        return docs;
    }

    @Override
    public void deleteIndex(String index) {
        try (solrClient) {
            solrClient.deleteByQuery(index, "*:*"); // deletes all documents
            solrClient.commit(index);
            log.info("All documents deleted from core: {}", index);
        } catch (Exception e) {
            log.error("Failed to delete documents: {}", e.getMessage());
        }
    }
}
