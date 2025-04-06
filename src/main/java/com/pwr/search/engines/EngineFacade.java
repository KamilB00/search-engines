package com.pwr.search.engines;

import com.pwr.search.wikipedia.WikipediaArticle;

import java.util.List;

public interface EngineFacade {

    SearchResult search(String index, String query);

    IndexArticlesResponse indexWikipediaArticles(List<WikipediaArticle> articles, String index);

    void deleteIndex(String index);
}
