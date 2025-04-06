### Contribution rules

1. Create your own branch from master
2. Don't merge changes without acceptance
3. Add engine integration in separate directories (e.g. `src/main/java/com/pwr/search/engines/solr`)
4. Add test class in the same package to conduct the experiment

### Configuration
- Configure the root directory with dataset in `application.properties` file
- Configure host, port and add other properties if necessary in `application.properties` to connect with your engine. When experimenting only with your own engine leave other properties set to TODO. If you want to experiment with other engines at the same time you need to add proper configuration and replace TODO words.
- Your engine connector should implement `EngineFacade` interface (e.g. `ElasticsearchWikipediaArticlesService`)

#### Example configuration of `application.properties` for Solr
```
spring.application.name=search-engines
root.directory.path=/Users/Your/Directory/data
elasticsearch.server.host=TODO
elasticsearch.server.port=TODO
elasticsearch.server.username=TODO
elasticsearch.server.password=TODO
solr.server.host=12.34.56.78
solr.server.port=1234
sphinx.server.host=TODO
sphinx.server.port=TODO
```

#### Example configuration of `application.properties` for Sphinx
```
spring.application.name=search-engines
root.directory.path=/Users/Your/Directory/data
elasticsearch.server.host=TODO
elasticsearch.server.port=TODO
elasticsearch.server.username=TODO
elasticsearch.server.password=TODO
solr.server.host=TODO
solr.server.port=TODO
sphinx.server.host=12.34.56.78
sphinx.server.port=1234
```