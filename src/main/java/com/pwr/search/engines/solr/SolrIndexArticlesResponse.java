package com.pwr.search.engines.solr;

import com.pwr.search.engines.IndexArticlesResponse;

public record SolrIndexArticlesResponse(long took, boolean isSuccessful) implements IndexArticlesResponse {
    @Override
    public long took() {
        return took;
    }

    @Override
    public boolean isSuccessful() {
        return isSuccessful;
    }


}
