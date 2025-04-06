package com.pwr.search.engines.elasticsearch;

import com.pwr.search.engines.Hit;
import com.pwr.search.wikipedia.WikipediaArticle;


public record ElasticsearchHit(int articleId, Double scoreBM25, String title) implements Hit {
    @Override
    public int articleId() {
        return articleId;
    }

    @Override
    public Double scoreBM25() {
        return scoreBM25;
    }

    @Override
    public String title() {
        return title;
    }

    public static Hit from(co.elastic.clients.elasticsearch.core.search.Hit<WikipediaArticle> hit) {
        WikipediaArticle wikipediaArticle = hit.source();
        return new ElasticsearchHit(wikipediaArticle.getId(), hit.score(), wikipediaArticle.getTitle());
    }
}
