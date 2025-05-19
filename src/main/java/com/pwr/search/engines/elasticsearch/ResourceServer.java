package com.pwr.search.engines.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search-engine")
@RequiredArgsConstructor
public class ResourceServer {

    private final ElasticsearchFacade elasticsearchFacade;

    @PostMapping("/index")
    public void index() {
        elasticsearchFacade.indexDocumentsAssigningRandomCategory();
    }
}
