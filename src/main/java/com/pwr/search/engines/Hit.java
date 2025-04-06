package com.pwr.search.engines;

public interface Hit {

    int articleId();

    Double scoreBM25();

    String title();

}
