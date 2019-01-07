package my.com.view.repository.search;

import my.com.view.domain.entity.Criteria;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Criteria entity.
 */
public interface CriteriaSearchRepository extends ElasticsearchRepository<Criteria, Long> {
}
