package com.pwr.search.engines.elasticsearch;

import com.pwr.search.wikipedia.WikipediaArticle;

public record WikipediaArticleWithCategory(
        WikipediaArticle wikipediaArticle,
        String category) {

    public int getId() {
        return wikipediaArticle.getId();
    }
}
