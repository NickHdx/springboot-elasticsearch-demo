package nick.tutorial.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import nick.tutorial.elasticsearch.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ArticleService {
	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	public void createIndex(String index) throws IOException {
		elasticsearchClient.indices().create(c -> c.index(index));
	}
}
