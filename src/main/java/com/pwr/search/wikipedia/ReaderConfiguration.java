package com.pwr.search.wikipedia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReaderConfiguration {

    @Bean
    public WikipediaArticlesRepository wikipediaArticlesProvider(@Value("${root.directory.path}") String rootDirectoryPath) {
        return new WikipediaArticlesCachingRepository(new DirectoryReader(rootDirectoryPath));
    }

}
