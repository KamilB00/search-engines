package com.pwr.search.engines.elasticsearch;

import com.pwr.search.engines.SearchResult;
import com.pwr.search.wikipedia.WikipediaArticlesRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticsearchFacade {
    private final WikipediaArticlesElasticsearchService articlesService;
    private final WikipediaArticlesRepository articlesRepository;
    private final WikipediaArticleCategoriesRepository categoriesRepository;

    public void indexDocumentsAssigningRandomCategory() {
        val allArticles = articlesRepository.getArticles();
        val allArticlesWithCategories = allArticles
                .stream().map(article -> new WikipediaArticleWithCategory(article, categoriesRepository.getRandomCategory()))
                .toList();
        articlesService.indexWikipediaArticles(allArticlesWithCategories, "articles");
    }

    public SearchResult search(String indexName, String text, int page, int size) {
        return articlesService.search(indexName, text, page, size);
    }
}