package my.com.view.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PermissionSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PermissionSearchRepositoryMockConfiguration {

    @MockBean
    private PermissionSearchRepository mockPermissionSearchRepository;

}
