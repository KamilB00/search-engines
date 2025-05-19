package com.pwr.search.engines.elasticsearch;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.pwr.search.engines.Hit;
import com.pwr.search.engines.SearchResult;
import com.pwr.search.wikipedia.WikipediaArticle;
import lombok.val;

import java.util.List;

public record ElasticsearchSearchResult(long totalHits,
                                        List<Hit> hits,
                                        boolean isSuccessful,
                                        long took,
                                        int page,
                                        int size) implements SearchResult {
    public static SearchResult success(SearchResponse<WikipediaArticle> response, int page, int size) {
        val hits = response.hits().hits().stream().map(ElasticsearchHit::from).toList();
        val totalHits = response.hits().total().value();
        val timeInMillis = response.took();
        return new ElasticsearchSearchResult(totalHits, hits, true, timeInMillis, page, size);
    }

    public static SearchResult failure(int page, int size) {
        return new ElasticsearchSearchResult(0, List.of(), false, 0, page, size);
    }
}
