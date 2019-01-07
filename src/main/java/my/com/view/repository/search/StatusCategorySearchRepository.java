package my.com.view.repository.search;

import my.com.view.domain.entity.StatusCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StatusCategory entity.
 */
public interface StatusCategorySearchRepository extends ElasticsearchRepository<StatusCategory, Long> {
}
