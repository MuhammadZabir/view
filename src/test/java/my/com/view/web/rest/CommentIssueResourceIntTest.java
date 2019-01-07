package my.com.view.web.rest;

import my.com.view.ViewApp;

import my.com.view.domain.entity.CommentIssue;
import my.com.view.repository.CommentIssueRepository;
import my.com.view.repository.search.CommentIssueSearchRepository;
import my.com.view.service.CommentIssueService;
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
 * Test class for the CommentIssueResource REST controller.
 *
 * @see CommentIssueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViewApp.class)
public class CommentIssueResourceIntTest {

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private CommentIssueRepository commentIssueRepository;



    @Autowired
    private CommentIssueService commentIssueService;

    /**
     * This repository is mocked in the my.com.view.repository.search test package.
     *
     * @see my.com.view.repository.search.CommentIssueSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommentIssueSearchRepository mockCommentIssueSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommentIssueMockMvc;

    private CommentIssue commentIssue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommentIssueResource commentIssueResource = new CommentIssueResource(commentIssueService);
        this.restCommentIssueMockMvc = MockMvcBuilders.standaloneSetup(commentIssueResource)
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
    public static CommentIssue createEntity(EntityManager em) {
        CommentIssue commentIssue = new CommentIssue()
            .comment(DEFAULT_COMMENT);
        return commentIssue;
    }

    @Before
    public void initTest() {
        commentIssue = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommentIssue() throws Exception {
        int databaseSizeBeforeCreate = commentIssueRepository.findAll().size();

        // Create the CommentIssue
        restCommentIssueMockMvc.perform(post("/api/comment-issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentIssue)))
            .andExpect(status().isCreated());

        // Validate the CommentIssue in the database
        List<CommentIssue> commentIssueList = commentIssueRepository.findAll();
        assertThat(commentIssueList).hasSize(databaseSizeBeforeCreate + 1);
        CommentIssue testCommentIssue = commentIssueList.get(commentIssueList.size() - 1);
        assertThat(testCommentIssue.getComment()).isEqualTo(DEFAULT_COMMENT);

        // Validate the CommentIssue in Elasticsearch
        verify(mockCommentIssueSearchRepository, times(1)).save(testCommentIssue);
    }

    @Test
    @Transactional
    public void createCommentIssueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commentIssueRepository.findAll().size();

        // Create the CommentIssue with an existing ID
        commentIssue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentIssueMockMvc.perform(post("/api/comment-issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentIssue)))
            .andExpect(status().isBadRequest());

        // Validate the CommentIssue in the database
        List<CommentIssue> commentIssueList = commentIssueRepository.findAll();
        assertThat(commentIssueList).hasSize(databaseSizeBeforeCreate);

        // Validate the CommentIssue in Elasticsearch
        verify(mockCommentIssueSearchRepository, times(0)).save(commentIssue);
    }

    @Test
    @Transactional
    public void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentIssueRepository.findAll().size();
        // set the field null
        commentIssue.setComment(null);

        // Create the CommentIssue, which fails.

        restCommentIssueMockMvc.perform(post("/api/comment-issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentIssue)))
            .andExpect(status().isBadRequest());

        List<CommentIssue> commentIssueList = commentIssueRepository.findAll();
        assertThat(commentIssueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommentIssues() throws Exception {
        // Initialize the database
        commentIssueRepository.saveAndFlush(commentIssue);

        // Get all the commentIssueList
        restCommentIssueMockMvc.perform(get("/api/comment-issues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentIssue.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }


    @Test
    @Transactional
    public void getCommentIssue() throws Exception {
        // Initialize the database
        commentIssueRepository.saveAndFlush(commentIssue);

        // Get the commentIssue
        restCommentIssueMockMvc.perform(get("/api/comment-issues/{id}", commentIssue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commentIssue.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCommentIssue() throws Exception {
        // Get the commentIssue
        restCommentIssueMockMvc.perform(get("/api/comment-issues/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommentIssue() throws Exception {
        // Initialize the database
        commentIssueService.save(commentIssue);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCommentIssueSearchRepository);

        int databaseSizeBeforeUpdate = commentIssueRepository.findAll().size();

        // Update the commentIssue
        CommentIssue updatedCommentIssue = commentIssueRepository.findById(commentIssue.getId()).get();
        // Disconnect from session so that the updates on updatedCommentIssue are not directly saved in db
        em.detach(updatedCommentIssue);
        updatedCommentIssue
            .comment(UPDATED_COMMENT);

        restCommentIssueMockMvc.perform(put("/api/comment-issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommentIssue)))
            .andExpect(status().isOk());

        // Validate the CommentIssue in the database
        List<CommentIssue> commentIssueList = commentIssueRepository.findAll();
        assertThat(commentIssueList).hasSize(databaseSizeBeforeUpdate);
        CommentIssue testCommentIssue = commentIssueList.get(commentIssueList.size() - 1);
        assertThat(testCommentIssue.getComment()).isEqualTo(UPDATED_COMMENT);

        // Validate the CommentIssue in Elasticsearch
        verify(mockCommentIssueSearchRepository, times(1)).save(testCommentIssue);
    }

    @Test
    @Transactional
    public void updateNonExistingCommentIssue() throws Exception {
        int databaseSizeBeforeUpdate = commentIssueRepository.findAll().size();

        // Create the CommentIssue

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCommentIssueMockMvc.perform(put("/api/comment-issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentIssue)))
            .andExpect(status().isBadRequest());

        // Validate the CommentIssue in the database
        List<CommentIssue> commentIssueList = commentIssueRepository.findAll();
        assertThat(commentIssueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentIssue in Elasticsearch
        verify(mockCommentIssueSearchRepository, times(0)).save(commentIssue);
    }

    @Test
    @Transactional
    public void deleteCommentIssue() throws Exception {
        // Initialize the database
        commentIssueService.save(commentIssue);

        int databaseSizeBeforeDelete = commentIssueRepository.findAll().size();

        // Get the commentIssue
        restCommentIssueMockMvc.perform(delete("/api/comment-issues/{id}", commentIssue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CommentIssue> commentIssueList = commentIssueRepository.findAll();
        assertThat(commentIssueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CommentIssue in Elasticsearch
        verify(mockCommentIssueSearchRepository, times(1)).deleteById(commentIssue.getId());
    }

    @Test
    @Transactional
    public void searchCommentIssue() throws Exception {
        // Initialize the database
        commentIssueService.save(commentIssue);
        when(mockCommentIssueSearchRepository.search(queryStringQuery("id:" + commentIssue.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(commentIssue), PageRequest.of(0, 1), 1));
        // Search the commentIssue
        restCommentIssueMockMvc.perform(get("/api/_search/comment-issues?query=id:" + commentIssue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentIssue.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentIssue.class);
        CommentIssue commentIssue1 = new CommentIssue();
        commentIssue1.setId(1L);
        CommentIssue commentIssue2 = new CommentIssue();
        commentIssue2.setId(commentIssue1.getId());
        assertThat(commentIssue1).isEqualTo(commentIssue2);
        commentIssue2.setId(2L);
        assertThat(commentIssue1).isNotEqualTo(commentIssue2);
        commentIssue1.setId(null);
        assertThat(commentIssue1).isNotEqualTo(commentIssue2);
    }
}
