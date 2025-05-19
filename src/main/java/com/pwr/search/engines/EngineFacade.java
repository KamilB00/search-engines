package com.pwr.search.engines;

import com.pwr.search.engines.elasticsearch.WikipediaArticleWithCategory;

import java.util.List;

public interface EngineFacade {
    SearchResult search(String index, String text, int page, int size);

    IndexArticlesResponse indexWikipediaArticles(List<WikipediaArticleWithCategory> articles, String index);

    void deleteIndex(String index);
}
