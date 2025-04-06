package com.pwr.search.wikipedia;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = WikipediaArticleDeserializer.class)
public class WikipediaArticle {

    private String title;

    private int id;

    private String url;

    private String text;

    private String article_type;
}
