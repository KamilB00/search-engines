### Contribution rules

1. Create your own branch from master
2. Don't merge changes without acceptance
3. Add engine integration in separate directories (e.g. src/main/java/com/pwr/search/engines/solr)
4. Add test class in the same package to conduct the experiment

### Configuration
- Configure the root directory with dataset in application.properties file
- Configure host, port and add other properties if necessary to connect with your engine
- Your engine connector should implement EngineFacade interface (e.g. ElasticsearchWikipediaArticlesService)

