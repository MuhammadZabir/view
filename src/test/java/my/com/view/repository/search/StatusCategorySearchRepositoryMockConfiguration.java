package my.com.view.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of StatusCategorySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class StatusCategorySearchRepositoryMockConfiguration {

    @MockBean
    private StatusCategorySearchRepository mockStatusCategorySearchRepository;

}
