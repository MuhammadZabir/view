package my.com.view.web.rest;

import my.com.view.ViewApp;

import my.com.view.domain.IssueDifficulty;
import my.com.view.repository.IssueDifficultyRepository;
import my.com.view.repository.search.IssueDifficultySearchRepository;
import my.com.view.service.IssueDifficultyService;
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
 * Test class for the IssueDifficultyResource REST controller.
 *
 * @see IssueDifficultyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViewApp.class)
public class IssueDifficultyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    @Autowired
    private IssueDifficultyRepository issueDifficultyRepository;

    

    @Autowired
    private IssueDifficultyService issueDifficultyService;

    /**
     * This repository is mocked in the my.com.view.repository.search test package.
     *
     * @see my.com.view.repository.search.IssueDifficultySearchRepositoryMockConfiguration
     */
    @Autowired
    private IssueDifficultySearchRepository mockIssueDifficultySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIssueDifficultyMockMvc;

    private IssueDifficulty issueDifficulty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IssueDifficultyResource issueDifficultyResource = new IssueDifficultyResource(issueDifficultyService);
        this.restIssueDifficultyMockMvc = MockMvcBuilders.standaloneSetup(issueDifficultyResource)
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
    public static IssueDifficulty createEntity(EntityManager em) {
        IssueDifficulty issueDifficulty = new IssueDifficulty()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE);
        return issueDifficulty;
    }

    @Before
    public void initTest() {
        issueDifficulty = createEntity(em);
    }

    @Test
    @Transactional
    public void createIssueDifficulty() throws Exception {
        int databaseSizeBeforeCreate = issueDifficultyRepository.findAll().size();

        // Create the IssueDifficulty
        restIssueDifficultyMockMvc.perform(post("/api/issue-difficulties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDifficulty)))
            .andExpect(status().isCreated());

        // Validate the IssueDifficulty in the database
        List<IssueDifficulty> issueDifficultyList = issueDifficultyRepository.findAll();
        assertThat(issueDifficultyList).hasSize(databaseSizeBeforeCreate + 1);
        IssueDifficulty testIssueDifficulty = issueDifficultyList.get(issueDifficultyList.size() - 1);
        assertThat(testIssueDifficulty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIssueDifficulty.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the IssueDifficulty in Elasticsearch
        verify(mockIssueDifficultySearchRepository, times(1)).save(testIssueDifficulty);
    }

    @Test
    @Transactional
    public void createIssueDifficultyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = issueDifficultyRepository.findAll().size();

        // Create the IssueDifficulty with an existing ID
        issueDifficulty.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssueDifficultyMockMvc.perform(post("/api/issue-difficulties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDifficulty)))
            .andExpect(status().isBadRequest());

        // Validate the IssueDifficulty in the database
        List<IssueDifficulty> issueDifficultyList = issueDifficultyRepository.findAll();
        assertThat(issueDifficultyList).hasSize(databaseSizeBeforeCreate);

        // Validate the IssueDifficulty in Elasticsearch
        verify(mockIssueDifficultySearchRepository, times(0)).save(issueDifficulty);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueDifficultyRepository.findAll().size();
        // set the field null
        issueDifficulty.setName(null);

        // Create the IssueDifficulty, which fails.

        restIssueDifficultyMockMvc.perform(post("/api/issue-difficulties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDifficulty)))
            .andExpect(status().isBadRequest());

        List<IssueDifficulty> issueDifficultyList = issueDifficultyRepository.findAll();
        assertThat(issueDifficultyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIssueDifficulties() throws Exception {
        // Initialize the database
        issueDifficultyRepository.saveAndFlush(issueDifficulty);

        // Get all the issueDifficultyList
        restIssueDifficultyMockMvc.perform(get("/api/issue-difficulties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issueDifficulty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }
    

    @Test
    @Transactional
    public void getIssueDifficulty() throws Exception {
        // Initialize the database
        issueDifficultyRepository.saveAndFlush(issueDifficulty);

        // Get the issueDifficulty
        restIssueDifficultyMockMvc.perform(get("/api/issue-difficulties/{id}", issueDifficulty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(issueDifficulty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingIssueDifficulty() throws Exception {
        // Get the issueDifficulty
        restIssueDifficultyMockMvc.perform(get("/api/issue-difficulties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIssueDifficulty() throws Exception {
        // Initialize the database
        issueDifficultyService.save(issueDifficulty);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockIssueDifficultySearchRepository);

        int databaseSizeBeforeUpdate = issueDifficultyRepository.findAll().size();

        // Update the issueDifficulty
        IssueDifficulty updatedIssueDifficulty = issueDifficultyRepository.findById(issueDifficulty.getId()).get();
        // Disconnect from session so that the updates on updatedIssueDifficulty are not directly saved in db
        em.detach(updatedIssueDifficulty);
        updatedIssueDifficulty
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE);

        restIssueDifficultyMockMvc.perform(put("/api/issue-difficulties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIssueDifficulty)))
            .andExpect(status().isOk());

        // Validate the IssueDifficulty in the database
        List<IssueDifficulty> issueDifficultyList = issueDifficultyRepository.findAll();
        assertThat(issueDifficultyList).hasSize(databaseSizeBeforeUpdate);
        IssueDifficulty testIssueDifficulty = issueDifficultyList.get(issueDifficultyList.size() - 1);
        assertThat(testIssueDifficulty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIssueDifficulty.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the IssueDifficulty in Elasticsearch
        verify(mockIssueDifficultySearchRepository, times(1)).save(testIssueDifficulty);
    }

    @Test
    @Transactional
    public void updateNonExistingIssueDifficulty() throws Exception {
        int databaseSizeBeforeUpdate = issueDifficultyRepository.findAll().size();

        // Create the IssueDifficulty

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIssueDifficultyMockMvc.perform(put("/api/issue-difficulties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDifficulty)))
            .andExpect(status().isBadRequest());

        // Validate the IssueDifficulty in the database
        List<IssueDifficulty> issueDifficultyList = issueDifficultyRepository.findAll();
        assertThat(issueDifficultyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IssueDifficulty in Elasticsearch
        verify(mockIssueDifficultySearchRepository, times(0)).save(issueDifficulty);
    }

    @Test
    @Transactional
    public void deleteIssueDifficulty() throws Exception {
        // Initialize the database
        issueDifficultyService.save(issueDifficulty);

        int databaseSizeBeforeDelete = issueDifficultyRepository.findAll().size();

        // Get the issueDifficulty
        restIssueDifficultyMockMvc.perform(delete("/api/issue-difficulties/{id}", issueDifficulty.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<IssueDifficulty> issueDifficultyList = issueDifficultyRepository.findAll();
        assertThat(issueDifficultyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IssueDifficulty in Elasticsearch
        verify(mockIssueDifficultySearchRepository, times(1)).deleteById(issueDifficulty.getId());
    }

    @Test
    @Transactional
    public void searchIssueDifficulty() throws Exception {
        // Initialize the database
        issueDifficultyService.save(issueDifficulty);
        when(mockIssueDifficultySearchRepository.search(queryStringQuery("id:" + issueDifficulty.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(issueDifficulty), PageRequest.of(0, 1), 1));
        // Search the issueDifficulty
        restIssueDifficultyMockMvc.perform(get("/api/_search/issue-difficulties?query=id:" + issueDifficulty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issueDifficulty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueDifficulty.class);
        IssueDifficulty issueDifficulty1 = new IssueDifficulty();
        issueDifficulty1.setId(1L);
        IssueDifficulty issueDifficulty2 = new IssueDifficulty();
        issueDifficulty2.setId(issueDifficulty1.getId());
        assertThat(issueDifficulty1).isEqualTo(issueDifficulty2);
        issueDifficulty2.setId(2L);
        assertThat(issueDifficulty1).isNotEqualTo(issueDifficulty2);
        issueDifficulty1.setId(null);
        assertThat(issueDifficulty1).isNotEqualTo(issueDifficulty2);
    }
}
