package com.pwr.search.engines.wikipedia;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class WikipediaArticlesCachingRepository implements WikipediaArticlesRepository {

    private final Map<Integer, WikipediaArticleDTO> articles = new HashMap<>();

    private final DirectoryReader directoryReader;

    @Override
    public Set<WikipediaArticleDTO> getArticles() {
        if (articles.isEmpty()) {
            loadAll();
        }
        return new HashSet<>(articles.values());
    }

    private void loadAll() {
        directoryReader.read().forEach(article -> articles.put(article.getId(), article));
    }
}
