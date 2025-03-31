package com.pwr.search.engines.wikipedia;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class WikipediaArticlesCachingRepository implements WikipediaArticlesRepository {

    private final Map<Integer, WikipediaArticle> articles = new HashMap<>();

    private final DirectoryReader directoryReader;

    @Override
    public List<WikipediaArticle> getArticles() {
        if (articles.isEmpty()) {
            loadAll();
        }
        return new ArrayList<>(articles.values());
    }

    private void loadAll() {
        directoryReader.read().forEach(article -> articles.put(article.getId(), article));
    }
}
