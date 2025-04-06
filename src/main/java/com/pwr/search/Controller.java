package com.pwr.search;

import com.pwr.search.wikipedia.WikipediaArticle;
import com.pwr.search.wikipedia.WikipediaArticlesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class Controller {

    private final WikipediaArticlesRepository wikipediaArticlesRepository;

    @GetMapping
    public Set<WikipediaArticle> tenArticles() {
        return wikipediaArticlesRepository.getArticles()
                .stream().limit(10)
                .collect(Collectors.toSet());
    }


}
