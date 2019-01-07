package my.com.view.web.rest;

import my.com.view.ViewApp;

import my.com.view.domain.entity.StatusCategory;
import my.com.view.repository.StatusCategoryRepository;
import my.com.view.repository.search.StatusCategorySearchRepository;
import my.com.view.service.StatusCategoryService;
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
 * Test class for the StatusCategoryResource REST controller.
 *
 * @see StatusCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViewApp.class)
public class StatusCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_POINT = 1D;
    private static final Double UPDATED_POINT = 2D;

    private static final Boolean DEFAULT_MAIN = false;
    private static final Boolean UPDATED_MAIN = true;

    @Autowired
    private StatusCategoryRepository statusCategoryRepository;



    @Autowired
    private StatusCategoryService statusCategoryService;

    /**
     * This repository is mocked in the my.com.view.repository.search test package.
     *
     * @see my.com.view.repository.search.StatusCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private StatusCategorySearchRepository mockStatusCategorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStatusCategoryMockMvc;

    private StatusCategory statusCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StatusCategoryResource statusCategoryResource = new StatusCategoryResource(statusCategoryService);
        this.restStatusCategoryMockMvc = MockMvcBuilders.standaloneSetup(statusCategoryResource)
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
    public static StatusCategory createEntity(EntityManager em) {
        StatusCategory statusCategory = new StatusCategory()
            .name(DEFAULT_NAME)
            .point(DEFAULT_POINT)
            .main(DEFAULT_MAIN);
        return statusCategory;
    }

    @Before
    public void initTest() {
        statusCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createStatusCategory() throws Exception {
        int databaseSizeBeforeCreate = statusCategoryRepository.findAll().size();

        // Create the StatusCategory
        restStatusCategoryMockMvc.perform(post("/api/status-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statusCategory)))
            .andExpect(status().isCreated());

        // Validate the StatusCategory in the database
        List<StatusCategory> statusCategoryList = statusCategoryRepository.findAll();
        assertThat(statusCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        StatusCategory testStatusCategory = statusCategoryList.get(statusCategoryList.size() - 1);
        assertThat(testStatusCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStatusCategory.getPoint()).isEqualTo(DEFAULT_POINT);
        assertThat(testStatusCategory.isMain()).isEqualTo(DEFAULT_MAIN);

        // Validate the StatusCategory in Elasticsearch
        verify(mockStatusCategorySearchRepository, times(1)).save(testStatusCategory);
    }

    @Test
    @Transactional
    public void createStatusCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = statusCategoryRepository.findAll().size();

        // Create the StatusCategory with an existing ID
        statusCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusCategoryMockMvc.perform(post("/api/status-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statusCategory)))
            .andExpect(status().isBadRequest());

        // Validate the StatusCategory in the database
        List<StatusCategory> statusCategoryList = statusCategoryRepository.findAll();
        assertThat(statusCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the StatusCategory in Elasticsearch
        verify(mockStatusCategorySearchRepository, times(0)).save(statusCategory);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = statusCategoryRepository.findAll().size();
        // set the field null
        statusCategory.setName(null);

        // Create the StatusCategory, which fails.

        restStatusCategoryMockMvc.perform(post("/api/status-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statusCategory)))
            .andExpect(status().isBadRequest());

        List<StatusCategory> statusCategoryList = statusCategoryRepository.findAll();
        assertThat(statusCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStatusCategories() throws Exception {
        // Initialize the database
        statusCategoryRepository.saveAndFlush(statusCategory);

        // Get all the statusCategoryList
        restStatusCategoryMockMvc.perform(get("/api/status-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT.doubleValue())))
            .andExpect(jsonPath("$.[*].main").value(hasItem(DEFAULT_MAIN.booleanValue())));
    }


    @Test
    @Transactional
    public void getStatusCategory() throws Exception {
        // Initialize the database
        statusCategoryRepository.saveAndFlush(statusCategory);

        // Get the statusCategory
        restStatusCategoryMockMvc.perform(get("/api/status-categories/{id}", statusCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(statusCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.point").value(DEFAULT_POINT.doubleValue()))
            .andExpect(jsonPath("$.main").value(DEFAULT_MAIN.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingStatusCategory() throws Exception {
        // Get the statusCategory
        restStatusCategoryMockMvc.perform(get("/api/status-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatusCategory() throws Exception {
        // Initialize the database
        statusCategoryService.save(statusCategory);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockStatusCategorySearchRepository);

        int databaseSizeBeforeUpdate = statusCategoryRepository.findAll().size();

        // Update the statusCategory
        StatusCategory updatedStatusCategory = statusCategoryRepository.findById(statusCategory.getId()).get();
        // Disconnect from session so that the updates on updatedStatusCategory are not directly saved in db
        em.detach(updatedStatusCategory);
        updatedStatusCategory
            .name(UPDATED_NAME)
            .point(UPDATED_POINT)
            .main(UPDATED_MAIN);

        restStatusCategoryMockMvc.perform(put("/api/status-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStatusCategory)))
            .andExpect(status().isOk());

        // Validate the StatusCategory in the database
        List<StatusCategory> statusCategoryList = statusCategoryRepository.findAll();
        assertThat(statusCategoryList).hasSize(databaseSizeBeforeUpdate);
        StatusCategory testStatusCategory = statusCategoryList.get(statusCategoryList.size() - 1);
        assertThat(testStatusCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStatusCategory.getPoint()).isEqualTo(UPDATED_POINT);
        assertThat(testStatusCategory.isMain()).isEqualTo(UPDATED_MAIN);

        // Validate the StatusCategory in Elasticsearch
        verify(mockStatusCategorySearchRepository, times(1)).save(testStatusCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingStatusCategory() throws Exception {
        int databaseSizeBeforeUpdate = statusCategoryRepository.findAll().size();

        // Create the StatusCategory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStatusCategoryMockMvc.perform(put("/api/status-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statusCategory)))
            .andExpect(status().isBadRequest());

        // Validate the StatusCategory in the database
        List<StatusCategory> statusCategoryList = statusCategoryRepository.findAll();
        assertThat(statusCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StatusCategory in Elasticsearch
        verify(mockStatusCategorySearchRepository, times(0)).save(statusCategory);
    }

    @Test
    @Transactional
    public void deleteStatusCategory() throws Exception {
        // Initialize the database
        statusCategoryService.save(statusCategory);

        int databaseSizeBeforeDelete = statusCategoryRepository.findAll().size();

        // Get the statusCategory
        restStatusCategoryMockMvc.perform(delete("/api/status-categories/{id}", statusCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StatusCategory> statusCategoryList = statusCategoryRepository.findAll();
        assertThat(statusCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StatusCategory in Elasticsearch
        verify(mockStatusCategorySearchRepository, times(1)).deleteById(statusCategory.getId());
    }

    @Test
    @Transactional
    public void searchStatusCategory() throws Exception {
        // Initialize the database
        statusCategoryService.save(statusCategory);
        when(mockStatusCategorySearchRepository.search(queryStringQuery("id:" + statusCategory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(statusCategory), PageRequest.of(0, 1), 1));
        // Search the statusCategory
        restStatusCategoryMockMvc.perform(get("/api/_search/status-categories?query=id:" + statusCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT.doubleValue())))
            .andExpect(jsonPath("$.[*].main").value(hasItem(DEFAULT_MAIN.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusCategory.class);
        StatusCategory statusCategory1 = new StatusCategory();
        statusCategory1.setId(1L);
        StatusCategory statusCategory2 = new StatusCategory();
        statusCategory2.setId(statusCategory1.getId());
        assertThat(statusCategory1).isEqualTo(statusCategory2);
        statusCategory2.setId(2L);
        assertThat(statusCategory1).isNotEqualTo(statusCategory2);
        statusCategory1.setId(null);
        assertThat(statusCategory1).isNotEqualTo(statusCategory2);
    }
}
