package my.com.view.repository.search;

import my.com.view.domain.entity.CommentIssue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CommentIssue entity.
 */
public interface CommentIssueSearchRepository extends ElasticsearchRepository<CommentIssue, Long> {
}
