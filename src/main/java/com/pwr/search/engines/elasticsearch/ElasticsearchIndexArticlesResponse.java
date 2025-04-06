package com.pwr.search.engines.elasticsearch;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.pwr.search.engines.IndexArticlesResponse;

public record ElasticsearchIndexArticlesResponse(long took, boolean isSuccessful) implements IndexArticlesResponse {

    @Override
    public long took() {
        return 0;
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }

    public static IndexArticlesResponse from(BulkResponse response) {
        boolean isSuccessful = !response.errors();
        long took = response.took();
        return new ElasticsearchIndexArticlesResponse(took, isSuccessful);
    }

    public static IndexArticlesResponse failure() {
        return new ElasticsearchIndexArticlesResponse(0, false);
    }
}
