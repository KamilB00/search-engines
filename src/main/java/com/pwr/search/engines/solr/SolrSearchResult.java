package com.pwr.search.engines.solr;

import com.pwr.search.engines.Hit;
import com.pwr.search.engines.SearchResult;

import java.util.List;

public record SolrSearchResult(long totalHits,
                               List<Hit> hits,
                               boolean isSuccessful,
                               long took) implements SearchResult {
}
