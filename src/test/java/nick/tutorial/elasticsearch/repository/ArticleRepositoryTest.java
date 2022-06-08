package nick.tutorial.elasticsearch.repository;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import nick.tutorial.elasticsearch.model.Article;
import nick.tutorial.elasticsearch.model.Author;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void addDocument() {
        Article article = new Article("Spring Data Elasticsearch V2");
        article.setAuthors(Lists.newArrayList(new Author("Nick Huang")));
        System.out.println(JSON.toJSONString(articleRepository.save(article)));
    }

    @Test
    public void findByAuthorsName() {
        System.out.println(JSON.toJSONString(articleRepository.findByAuthorsName("Nick Huang")));
    }

    @Test
    public void findByTitle() {
        System.out.println(JSON.toJSONString(articleRepository.findByTitle("Spring Data Elasticsearch V2")));
    }

    @Test
    public void findByQueryTitle() {
        System.out.println(JSON.toJSONString(articleRepository.findByQueryTitle("V2")));
    }
}