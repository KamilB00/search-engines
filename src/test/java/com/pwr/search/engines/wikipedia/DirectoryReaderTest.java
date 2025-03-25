package com.pwr.search.engines.wikipedia;


import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectoryReaderTest {

    @Test
    void shouldReadTextJSONFileInDirectoryAndSkipImgSubDirectory() {
        // given
        String rootDirectoryPath = "src/test/resources/root";
        val articles = new WikipediaArticlesCachingRepository(new DirectoryReader(rootDirectoryPath)).getArticles();
        val articlesIds = articles.stream()
                .map(WikipediaArticleDTO::getId)
                .collect(Collectors.toSet());

        val expectedArticlesIds = Set.of(1919394, 1919393);
        // expect
        assertEquals(2, articles.size());
        assertTrue(articlesIds.containsAll(expectedArticlesIds));
    }
}
