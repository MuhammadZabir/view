package my.com.view.repository.search;

import my.com.view.domain.entity.Permission;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Permission entity.
 */
public interface PermissionSearchRepository extends ElasticsearchRepository<Permission, Long> {
}
