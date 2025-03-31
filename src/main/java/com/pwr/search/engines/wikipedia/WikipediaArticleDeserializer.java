package com.pwr.search.engines.wikipedia;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class WikipediaArticleDeserializer extends JsonDeserializer<WikipediaArticle> {

    @Override
    public WikipediaArticle deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectNode node = jsonParser.getCodec().readTree(jsonParser);
        String title = node.get("title").asText();
        int id = node.get("id").asInt();
        String url = node.get("url").asText();
        String text = node.get("text").asText();
        String articleType = node.get("article_type").asText();
        return new WikipediaArticle(title, id, url, text, articleType);
    }
}
