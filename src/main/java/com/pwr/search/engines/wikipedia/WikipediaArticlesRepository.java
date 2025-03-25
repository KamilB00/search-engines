package com.pwr.search.engines.wikipedia;

import java.util.Set;

public interface WikipediaArticlesRepository {

    Set<WikipediaArticleDTO> getArticles();
}
