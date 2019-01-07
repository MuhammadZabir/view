package my.com.view.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of IssueDifficultySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class IssueDifficultySearchRepositoryMockConfiguration {

    @MockBean
    private IssueDifficultySearchRepository mockIssueDifficultySearchRepository;

}
