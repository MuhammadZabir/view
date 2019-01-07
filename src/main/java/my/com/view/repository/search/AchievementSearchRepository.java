package my.com.view.repository.search;

import my.com.view.domain.entity.Achievement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Achievement entity.
 */
public interface AchievementSearchRepository extends ElasticsearchRepository<Achievement, Long> {
}
