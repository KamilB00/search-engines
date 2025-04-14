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

Solr indexing times csv:

Update, Commit, Total
4845,361,5206
4586,336,4922
4439,320,4759
4392,312,4704
4344,328,4672
4253,309,4562
4457,339,4796
4667,368,5035
4543,365,4908
4580,377,4957
5243,394,5637
4531,1216,5747
4385,347,4732
4533,374,4907
4813,369,5182
5245,1119,6364
4175,307,4482
4131,304,4435
4631,397,5028
4223,325,4548
4336,342,4678
4978,399,5377
4097,1062,5159
4053,302,4355
4366,339,4705
4919,388,5307
4232,683,4915
4157,319,4476
4028,296,4324
4320,327,4647
4086,306,4392
3964,295,4259
4121,361,4482
4801,406,5207
5408,1870,7278
4072,298,4370
125,107,232

