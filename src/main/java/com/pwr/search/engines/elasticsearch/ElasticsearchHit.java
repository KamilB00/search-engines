package com.pwr.search.engines.elasticsearch;

import com.pwr.search.engines.Hit;
import com.pwr.search.wikipedia.WikipediaArticle;

import java.util.List;

public record ElasticsearchHit(int articleId, Double scoreBM25, String title, String url, List<String> categories) implements Hit {
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

    @Override
    public String url() {
        return url;
    }

    @Override
    public List<String> categories() {
        return categories;
    }

    public static Hit from(co.elastic.clients.elasticsearch.core.search.Hit<WikipediaArticle> hit) {
        WikipediaArticle wikipediaArticle = hit.source();
        return new ElasticsearchHit(wikipediaArticle.getId(), hit.score(), wikipediaArticle.getTitle(), wikipediaArticle.getUrl(), wikipediaArticle.getCategories());
    }
}
