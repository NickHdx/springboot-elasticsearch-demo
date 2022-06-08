# Spring boot + Elasticsearch

___

## Requirements
Requires an installation of [Elasticsearch](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/install-elasticsearch.html). And [Kibana](https://www.elastic.co/guide/en/kibana/7.17/install.html), which offers a GUI to manage Elasticsearch, is optional.

The versions this tutorial uses. 
```
elasticsearch: 7.17.3
kibana: 7.17.3
jdk: 1.8.0_291
```

#### FAQ About Installation of Elasticsearch
1. If you use install Elasticsearch with docker desktop which is deployed on Windows WSL subsystem, this error `Max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]` may occur when starting Elasticsearch.

Run the following command in powershell to login docker desktop,
```shell
wsl -d docker-desktop
```
then to set `vm.max_map_count`.
```shell
sysctl -w vm.max_map_count=262144
```
To persist the change, add `vm.max_map_count=262144` to `/etc/sysctl.conf`.

To verify after rebooting docker-desktop, run
```shell 
sysctl vm.max_map_count
```

If above commands are not effective, try to add `vm.max_map_count` setting in `C:\Users\<username>\.wslconfig`,
```text
[wsl2]
kernelCommandLine = "sysctl.vm.max_map_count=262144"
```
then restart WSL. This global setting will affect all subsystem instances in WSL.
```shell
wsl --shutdown
```

2. For Elasticsearch version 8.0+, this error `org.elasticsearch.ElasticsearchException: not all primary shards of [.geoip_databases] index are active` may occur when starting Elasticsearch.

You can disable geoip downloader by adding the following setting in `<ES_Home>\config\elasticsearch.yml`, then restart Elasticsearch.
```yaml
ingest.geoip.downloader.enabled: false
```

3. For Elasticsearch version 8.0+, this error `curl: (60) schannel: CertGetCertificateChain trust error CERT_TRUST_REVOCATION_STATUS_UNKNOWN` may occur if you call Elasticsearch RESTful API by cUrl.
   
Elasticsearch 8.0+ defaults to enable security features. You can ignore this error by adding `--insecure` or `-k` in cUrl command.
```shell
curl --insecure --cacert http_ca.crt -u elastic https://localhost:9200
```

4. For Elasticsearch version 8.0+, security features are not friendly to the Elasticsearch trial, you can disable security features by modifying the following setting in `<ES_Home>\config\elasticsearch.yml`, then restart Elasticsearch.
```yaml
xpack.security.enabled: false
```

## Maven dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-elasticsearch</artifactId>
    <version>4.4.0</version>
    <exclusions>
        <exclusion>
            <groupId>jakarta.json</groupId>
            <artifactId>jakarta.json-api</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.13.3</version>
</dependency>
<dependency>
    <groupId>jakarta.json</groupId>
    <artifactId>jakarta.json-api</artifactId>
    <version>2.1.0</version>
</dependency>
```
Please make sure that Spring Data Elasticsearch version, Spring Boot version and Elasticsearch version you have installed are compatible by checking this table [Versions](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#preface.versions).

## Mappings
The mappings define a schema for documents. The following code snippet defines an entity `Article`, it is a sample document which belongs to index `blog`.
```java
@Document(indexName = "blog")
public class Article {
    @Id
    private String id;
    @Field(type = Text)
    private String title;
    
    ...
}
```

Spring Data Elasticsearch generally auto-creates indexes based on the entities in the project.

## Spring Data Repositories
Spring Data Repository is the abstraction of Elasticsearch RESTful API. It makes us operate Elasticsearch just like JDBC.

The following code snippet defines a ArticleRepository to query articles from Elasticsearch.
```java
@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {
    List<Article> findByAuthorsName(String name);
    List<Article> findByTitle(String title);
    
    ...
}
```

## Configuring Elasticsearch
To define how to connect to the Elasticsearch instance, we use ElasticsearchClient to configure it.
```java
@Configuration
public class Config {
    @Bean
    public ElasticsearchClient client() {
        // Create the low-level client
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // And create the API client
        return new ElasticsearchClient(transport);
    }
}
```
## Further
* Use logstash to sync data from Database to Elasticsearch