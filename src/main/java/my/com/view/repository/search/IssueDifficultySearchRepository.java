package my.com.view.repository.search;

import my.com.view.domain.entity.IssueDifficulty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the IssueDifficulty entity.
 */
public interface IssueDifficultySearchRepository extends ElasticsearchRepository<IssueDifficulty, Long> {
}
