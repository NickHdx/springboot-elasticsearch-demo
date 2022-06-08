package nick.tutorial.elasticsearch.repository;

import nick.tutorial.elasticsearch.model.Article;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {
    List<Article> findByAuthorsName(String name);

    List<Article> findByTitle(String title);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"title\": \"?0\"}}]}}")
    SearchHits<Article> findByQueryTitle(String title);
}
