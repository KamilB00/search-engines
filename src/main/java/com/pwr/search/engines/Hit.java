package com.pwr.search.engines;

import java.util.List;

public interface Hit {
    int articleId();

    Double scoreBM25();

    String title();

    String url();

    List<String> categories();
}
