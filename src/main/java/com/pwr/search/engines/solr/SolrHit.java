package com.pwr.search.engines.solr;

import com.pwr.search.engines.Hit;

public record SolrHit(int articleId, Double scoreBM25, String title) implements Hit {
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
}
