package my.com.view.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CommentIssueSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CommentIssueSearchRepositoryMockConfiguration {

    @MockBean
    private CommentIssueSearchRepository mockCommentIssueSearchRepository;

}
