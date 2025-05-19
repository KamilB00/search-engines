package com.pwr.search.engines;

import com.pwr.search.engines.elasticsearch.WikipediaArticleWithCategory;

import java.util.List;

public interface EngineFacade {

    SearchResult search(String index, String query);

    IndexArticlesResponse indexWikipediaArticles(List<WikipediaArticleWithCategory> articles, String index);

    void deleteIndex(String index);
}
