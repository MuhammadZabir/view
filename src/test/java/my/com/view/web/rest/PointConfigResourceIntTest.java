package my.com.view.web.rest;

import my.com.view.ViewApp;

import my.com.view.domain.entity.PointConfig;
import my.com.view.repository.PointConfigRepository;
import my.com.view.repository.search.PointConfigSearchRepository;
import my.com.view.service.PointConfigService;
import my.com.view.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static my.com.view.web.rest.TestUtil.sameInstant;
import static my.com.view.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PointConfigResource REST controller.
 *
 * @see PointConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViewApp.class)
public class PointConfigResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private PointConfigRepository pointConfigRepository;



    @Autowired
    private PointConfigService pointConfigService;

    /**
     * This repository is mocked in the my.com.view.repository.search test package.
     *
     * @see my.com.view.repository.search.PointConfigSearchRepositoryMockConfiguration
     */
    @Autowired
    private PointConfigSearchRepository mockPointConfigSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPointConfigMockMvc;

    private PointConfig pointConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PointConfigResource pointConfigResource = new PointConfigResource(pointConfigService);
        this.restPointConfigMockMvc = MockMvcBuilders.standaloneSetup(pointConfigResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointConfig createEntity(EntityManager em) {
        PointConfig pointConfig = new PointConfig()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return pointConfig;
    }

    @Before
    public void initTest() {
        pointConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createPointConfig() throws Exception {
        int databaseSizeBeforeCreate = pointConfigRepository.findAll().size();

        // Create the PointConfig
        restPointConfigMockMvc.perform(post("/api/point-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointConfig)))
            .andExpect(status().isCreated());

        // Validate the PointConfig in the database
        List<PointConfig> pointConfigList = pointConfigRepository.findAll();
        assertThat(pointConfigList).hasSize(databaseSizeBeforeCreate + 1);
        PointConfig testPointConfig = pointConfigList.get(pointConfigList.size() - 1);
        assertThat(testPointConfig.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPointConfig.getEndDate()).isEqualTo(DEFAULT_END_DATE);

        // Validate the PointConfig in Elasticsearch
        verify(mockPointConfigSearchRepository, times(1)).save(testPointConfig);
    }

    @Test
    @Transactional
    public void createPointConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pointConfigRepository.findAll().size();

        // Create the PointConfig with an existing ID
        pointConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointConfigMockMvc.perform(post("/api/point-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointConfig)))
            .andExpect(status().isBadRequest());

        // Validate the PointConfig in the database
        List<PointConfig> pointConfigList = pointConfigRepository.findAll();
        assertThat(pointConfigList).hasSize(databaseSizeBeforeCreate);

        // Validate the PointConfig in Elasticsearch
        verify(mockPointConfigSearchRepository, times(0)).save(pointConfig);
    }

    @Test
    @Transactional
    public void getAllPointConfigs() throws Exception {
        // Initialize the database
        pointConfigRepository.saveAndFlush(pointConfig);

        // Get all the pointConfigList
        restPointConfigMockMvc.perform(get("/api/point-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))));
    }


    @Test
    @Transactional
    public void getPointConfig() throws Exception {
        // Initialize the database
        pointConfigRepository.saveAndFlush(pointConfig);

        // Get the pointConfig
        restPointConfigMockMvc.perform(get("/api/point-configs/{id}", pointConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pointConfig.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)));
    }
    @Test
    @Transactional
    public void getNonExistingPointConfig() throws Exception {
        // Get the pointConfig
        restPointConfigMockMvc.perform(get("/api/point-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePointConfig() throws Exception {
        // Initialize the database
        pointConfigService.save(pointConfig);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPointConfigSearchRepository);

        int databaseSizeBeforeUpdate = pointConfigRepository.findAll().size();

        // Update the pointConfig
        PointConfig updatedPointConfig = pointConfigRepository.findById(pointConfig.getId()).get();
        // Disconnect from session so that the updates on updatedPointConfig are not directly saved in db
        em.detach(updatedPointConfig);
        updatedPointConfig
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restPointConfigMockMvc.perform(put("/api/point-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPointConfig)))
            .andExpect(status().isOk());

        // Validate the PointConfig in the database
        List<PointConfig> pointConfigList = pointConfigRepository.findAll();
        assertThat(pointConfigList).hasSize(databaseSizeBeforeUpdate);
        PointConfig testPointConfig = pointConfigList.get(pointConfigList.size() - 1);
        assertThat(testPointConfig.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPointConfig.getEndDate()).isEqualTo(UPDATED_END_DATE);

        // Validate the PointConfig in Elasticsearch
        verify(mockPointConfigSearchRepository, times(1)).save(testPointConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingPointConfig() throws Exception {
        int databaseSizeBeforeUpdate = pointConfigRepository.findAll().size();

        // Create the PointConfig

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPointConfigMockMvc.perform(put("/api/point-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointConfig)))
            .andExpect(status().isBadRequest());

        // Validate the PointConfig in the database
        List<PointConfig> pointConfigList = pointConfigRepository.findAll();
        assertThat(pointConfigList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PointConfig in Elasticsearch
        verify(mockPointConfigSearchRepository, times(0)).save(pointConfig);
    }

    @Test
    @Transactional
    public void deletePointConfig() throws Exception {
        // Initialize the database
        pointConfigService.save(pointConfig);

        int databaseSizeBeforeDelete = pointConfigRepository.findAll().size();

        // Get the pointConfig
        restPointConfigMockMvc.perform(delete("/api/point-configs/{id}", pointConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PointConfig> pointConfigList = pointConfigRepository.findAll();
        assertThat(pointConfigList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PointConfig in Elasticsearch
        verify(mockPointConfigSearchRepository, times(1)).deleteById(pointConfig.getId());
    }

    @Test
    @Transactional
    public void searchPointConfig() throws Exception {
        // Initialize the database
        pointConfigService.save(pointConfig);
        when(mockPointConfigSearchRepository.search(queryStringQuery("id:" + pointConfig.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(pointConfig), PageRequest.of(0, 1), 1));
        // Search the pointConfig
        restPointConfigMockMvc.perform(get("/api/_search/point-configs?query=id:" + pointConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointConfig.class);
        PointConfig pointConfig1 = new PointConfig();
        pointConfig1.setId(1L);
        PointConfig pointConfig2 = new PointConfig();
        pointConfig2.setId(pointConfig1.getId());
        assertThat(pointConfig1).isEqualTo(pointConfig2);
        pointConfig2.setId(2L);
        assertThat(pointConfig1).isNotEqualTo(pointConfig2);
        pointConfig1.setId(null);
        assertThat(pointConfig1).isNotEqualTo(pointConfig2);
    }
}
