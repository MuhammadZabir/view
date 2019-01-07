package my.com.view.repository.search;

import my.com.view.domain.entity.PointConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PointConfig entity.
 */
public interface PointConfigSearchRepository extends ElasticsearchRepository<PointConfig, Long> {
}
