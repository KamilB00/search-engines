package com.pwr.search.engines.solr;

import com.pwr.search.engines.IndexArticlesResponse;

public record SolrIndexArticlesResponse(long took, boolean isSuccessful, long commitTime, long updateTime) implements IndexArticlesResponse {
    @Override
    public long took() {
        return took;
    }

    @Override
    public boolean isSuccessful() {
        return isSuccessful;
    }

    public long commitTime() { return commitTime; }
    public long updateTime() { return updateTime; }
}
