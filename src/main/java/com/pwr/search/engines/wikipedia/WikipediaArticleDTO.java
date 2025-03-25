package com.pwr.search.engines.wikipedia;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = WikipediaArticleDTODeserializer.class)
public class WikipediaArticleDTO {

    private String title;

    private int id;

    private String url;

    private String text;

    private String article_type;
}
