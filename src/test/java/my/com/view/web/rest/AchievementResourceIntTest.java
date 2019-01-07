package my.com.view.web.rest;

import my.com.view.ViewApp;

import my.com.view.domain.entity.Achievement;
import my.com.view.repository.AchievementRepository;
import my.com.view.repository.search.AchievementSearchRepository;
import my.com.view.service.AchievementService;
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
 * Test class for the AchievementResource REST controller.
 *
 * @see AchievementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViewApp.class)
public class AchievementResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private AchievementRepository achievementRepository;



    @Autowired
    private AchievementService achievementService;

    /**
     * This repository is mocked in the my.com.view.repository.search test package.
     *
     * @see my.com.view.repository.search.AchievementSearchRepositoryMockConfiguration
     */
    @Autowired
    private AchievementSearchRepository mockAchievementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAchievementMockMvc;

    private Achievement achievement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AchievementResource achievementResource = new AchievementResource(achievementService);
        this.restAchievementMockMvc = MockMvcBuilders.standaloneSetup(achievementResource)
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
    public static Achievement createEntity(EntityManager em) {
        Achievement achievement = new Achievement()
            .status(DEFAULT_STATUS);
        return achievement;
    }

    @Before
    public void initTest() {
        achievement = createEntity(em);
    }

    @Test
    @Transactional
    public void createAchievement() throws Exception {
        int databaseSizeBeforeCreate = achievementRepository.findAll().size();

        // Create the Achievement
        restAchievementMockMvc.perform(post("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isCreated());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeCreate + 1);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Achievement in Elasticsearch
        verify(mockAchievementSearchRepository, times(1)).save(testAchievement);
    }

    @Test
    @Transactional
    public void createAchievementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = achievementRepository.findAll().size();

        // Create the Achievement with an existing ID
        achievement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAchievementMockMvc.perform(post("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeCreate);

        // Validate the Achievement in Elasticsearch
        verify(mockAchievementSearchRepository, times(0)).save(achievement);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = achievementRepository.findAll().size();
        // set the field null
        achievement.setStatus(null);

        // Create the Achievement, which fails.

        restAchievementMockMvc.perform(post("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isBadRequest());

        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAchievements() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        // Get all the achievementList
        restAchievementMockMvc.perform(get("/api/achievements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(achievement.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }


    @Test
    @Transactional
    public void getAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        // Get the achievement
        restAchievementMockMvc.perform(get("/api/achievements/{id}", achievement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(achievement.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAchievement() throws Exception {
        // Get the achievement
        restAchievementMockMvc.perform(get("/api/achievements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAchievement() throws Exception {
        // Initialize the database
        achievementService.save(achievement);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAchievementSearchRepository);

        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Update the achievement
        Achievement updatedAchievement = achievementRepository.findById(achievement.getId()).get();
        // Disconnect from session so that the updates on updatedAchievement are not directly saved in db
        em.detach(updatedAchievement);
        updatedAchievement
            .status(UPDATED_STATUS);

        restAchievementMockMvc.perform(put("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAchievement)))
            .andExpect(status().isOk());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Achievement in Elasticsearch
        verify(mockAchievementSearchRepository, times(1)).save(testAchievement);
    }

    @Test
    @Transactional
    public void updateNonExistingAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Create the Achievement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAchievementMockMvc.perform(put("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Achievement in Elasticsearch
        verify(mockAchievementSearchRepository, times(0)).save(achievement);
    }

    @Test
    @Transactional
    public void deleteAchievement() throws Exception {
        // Initialize the database
        achievementService.save(achievement);

        int databaseSizeBeforeDelete = achievementRepository.findAll().size();

        // Get the achievement
        restAchievementMockMvc.perform(delete("/api/achievements/{id}", achievement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Achievement in Elasticsearch
        verify(mockAchievementSearchRepository, times(1)).deleteById(achievement.getId());
    }

    @Test
    @Transactional
    public void searchAchievement() throws Exception {
        // Initialize the database
        achievementService.save(achievement);
        when(mockAchievementSearchRepository.search(queryStringQuery("id:" + achievement.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(achievement), PageRequest.of(0, 1), 1));
        // Search the achievement
        restAchievementMockMvc.perform(get("/api/_search/achievements?query=id:" + achievement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(achievement.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Achievement.class);
        Achievement achievement1 = new Achievement();
        achievement1.setId(1L);
        Achievement achievement2 = new Achievement();
        achievement2.setId(achievement1.getId());
        assertThat(achievement1).isEqualTo(achievement2);
        achievement2.setId(2L);
        assertThat(achievement1).isNotEqualTo(achievement2);
        achievement1.setId(null);
        assertThat(achievement1).isNotEqualTo(achievement2);
    }
}
