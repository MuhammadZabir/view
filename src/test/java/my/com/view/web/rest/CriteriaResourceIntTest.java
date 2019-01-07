package my.com.view.web.rest;

import my.com.view.ViewApp;

import my.com.view.domain.entity.Criteria;
import my.com.view.repository.CriteriaRepository;
import my.com.view.repository.search.CriteriaSearchRepository;
import my.com.view.service.CriteriaService;
import my.com.view.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static my.com.view.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CriteriaResource REST controller.
 *
 * @see CriteriaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViewApp.class)
public class CriteriaResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_LEVEL = 1L;
    private static final Long UPDATED_LEVEL = 2L;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private CriteriaRepository criteriaRepository;
    @Mock
    private CriteriaRepository criteriaRepositoryMock;

    @Mock
    private CriteriaService criteriaServiceMock;

    @Autowired
    private CriteriaService criteriaService;

    /**
     * This repository is mocked in the my.com.view.repository.search test package.
     *
     * @see my.com.view.repository.search.CriteriaSearchRepositoryMockConfiguration
     */
    @Autowired
    private CriteriaSearchRepository mockCriteriaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCriteriaMockMvc;

    private Criteria criteria;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CriteriaResource criteriaResource = new CriteriaResource(criteriaService);
        this.restCriteriaMockMvc = MockMvcBuilders.standaloneSetup(criteriaResource)
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
    public static Criteria createEntity(EntityManager em) {
        Criteria criteria = new Criteria()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .level(DEFAULT_LEVEL)
            .type(DEFAULT_TYPE);
        return criteria;
    }

    @Before
    public void initTest() {
        criteria = createEntity(em);
    }

    @Test
    @Transactional
    public void createCriteria() throws Exception {
        int databaseSizeBeforeCreate = criteriaRepository.findAll().size();

        // Create the Criteria
        restCriteriaMockMvc.perform(post("/api/criteria")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criteria)))
            .andExpect(status().isCreated());

        // Validate the Criteria in the database
        List<Criteria> criteriaList = criteriaRepository.findAll();
        assertThat(criteriaList).hasSize(databaseSizeBeforeCreate + 1);
        Criteria testCriteria = criteriaList.get(criteriaList.size() - 1);
        assertThat(testCriteria.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCriteria.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCriteria.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testCriteria.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Criteria in Elasticsearch
        verify(mockCriteriaSearchRepository, times(1)).save(testCriteria);
    }

    @Test
    @Transactional
    public void createCriteriaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = criteriaRepository.findAll().size();

        // Create the Criteria with an existing ID
        criteria.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCriteriaMockMvc.perform(post("/api/criteria")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criteria)))
            .andExpect(status().isBadRequest());

        // Validate the Criteria in the database
        List<Criteria> criteriaList = criteriaRepository.findAll();
        assertThat(criteriaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Criteria in Elasticsearch
        verify(mockCriteriaSearchRepository, times(0)).save(criteria);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = criteriaRepository.findAll().size();
        // set the field null
        criteria.setName(null);

        // Create the Criteria, which fails.

        restCriteriaMockMvc.perform(post("/api/criteria")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criteria)))
            .andExpect(status().isBadRequest());

        List<Criteria> criteriaList = criteriaRepository.findAll();
        assertThat(criteriaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCriteria() throws Exception {
        // Initialize the database
        criteriaRepository.saveAndFlush(criteria);

        // Get all the criteriaList
        restCriteriaMockMvc.perform(get("/api/criteria?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(criteria.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    public void getAllCriteriaWithEagerRelationshipsIsEnabled() throws Exception {
        CriteriaResource criteriaResource = new CriteriaResource(criteriaServiceMock);
        when(criteriaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restCriteriaMockMvc = MockMvcBuilders.standaloneSetup(criteriaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCriteriaMockMvc.perform(get("/api/criteria?eagerload=true"))
        .andExpect(status().isOk());

        verify(criteriaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllCriteriaWithEagerRelationshipsIsNotEnabled() throws Exception {
        CriteriaResource criteriaResource = new CriteriaResource(criteriaServiceMock);
            when(criteriaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restCriteriaMockMvc = MockMvcBuilders.standaloneSetup(criteriaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCriteriaMockMvc.perform(get("/api/criteria?eagerload=true"))
        .andExpect(status().isOk());

            verify(criteriaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCriteria() throws Exception {
        // Initialize the database
        criteriaRepository.saveAndFlush(criteria);

        // Get the criteria
        restCriteriaMockMvc.perform(get("/api/criteria/{id}", criteria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(criteria.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCriteria() throws Exception {
        // Get the criteria
        restCriteriaMockMvc.perform(get("/api/criteria/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCriteria() throws Exception {
        // Initialize the database
        criteriaService.save(criteria);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCriteriaSearchRepository);

        int databaseSizeBeforeUpdate = criteriaRepository.findAll().size();

        // Update the criteria
        Criteria updatedCriteria = criteriaRepository.findById(criteria.getId()).get();
        // Disconnect from session so that the updates on updatedCriteria are not directly saved in db
        em.detach(updatedCriteria);
        updatedCriteria
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .level(UPDATED_LEVEL)
            .type(UPDATED_TYPE);

        restCriteriaMockMvc.perform(put("/api/criteria")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCriteria)))
            .andExpect(status().isOk());

        // Validate the Criteria in the database
        List<Criteria> criteriaList = criteriaRepository.findAll();
        assertThat(criteriaList).hasSize(databaseSizeBeforeUpdate);
        Criteria testCriteria = criteriaList.get(criteriaList.size() - 1);
        assertThat(testCriteria.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCriteria.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCriteria.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testCriteria.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Criteria in Elasticsearch
        verify(mockCriteriaSearchRepository, times(1)).save(testCriteria);
    }

    @Test
    @Transactional
    public void updateNonExistingCriteria() throws Exception {
        int databaseSizeBeforeUpdate = criteriaRepository.findAll().size();

        // Create the Criteria

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCriteriaMockMvc.perform(put("/api/criteria")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criteria)))
            .andExpect(status().isBadRequest());

        // Validate the Criteria in the database
        List<Criteria> criteriaList = criteriaRepository.findAll();
        assertThat(criteriaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Criteria in Elasticsearch
        verify(mockCriteriaSearchRepository, times(0)).save(criteria);
    }

    @Test
    @Transactional
    public void deleteCriteria() throws Exception {
        // Initialize the database
        criteriaService.save(criteria);

        int databaseSizeBeforeDelete = criteriaRepository.findAll().size();

        // Get the criteria
        restCriteriaMockMvc.perform(delete("/api/criteria/{id}", criteria.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Criteria> criteriaList = criteriaRepository.findAll();
        assertThat(criteriaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Criteria in Elasticsearch
        verify(mockCriteriaSearchRepository, times(1)).deleteById(criteria.getId());
    }

    @Test
    @Transactional
    public void searchCriteria() throws Exception {
        // Initialize the database
        criteriaService.save(criteria);
        when(mockCriteriaSearchRepository.search(queryStringQuery("id:" + criteria.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(criteria), PageRequest.of(0, 1), 1));
        // Search the criteria
        restCriteriaMockMvc.perform(get("/api/_search/criteria?query=id:" + criteria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(criteria.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Criteria.class);
        Criteria criteria1 = new Criteria();
        criteria1.setId(1L);
        Criteria criteria2 = new Criteria();
        criteria2.setId(criteria1.getId());
        assertThat(criteria1).isEqualTo(criteria2);
        criteria2.setId(2L);
        assertThat(criteria1).isNotEqualTo(criteria2);
        criteria1.setId(null);
        assertThat(criteria1).isNotEqualTo(criteria2);
    }
}
