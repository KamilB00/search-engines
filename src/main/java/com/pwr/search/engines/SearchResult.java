package com.pwr.search.engines;

import java.util.List;

public interface SearchResult {
    long totalHits();

    List<Hit> hits();

    boolean isSuccessful();

    long took();

    int page();

    int size();
}
