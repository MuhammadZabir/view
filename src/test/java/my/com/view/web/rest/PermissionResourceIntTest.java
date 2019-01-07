package my.com.view.web.rest;

import my.com.view.ViewApp;

import my.com.view.domain.entity.Permission;
import my.com.view.repository.PermissionRepository;
import my.com.view.repository.search.PermissionSearchRepository;
import my.com.view.service.PermissionService;
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
 * Test class for the PermissionResource REST controller.
 *
 * @see PermissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViewApp.class)
public class PermissionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    @Autowired
    private PermissionRepository permissionRepository;
    @Mock
    private PermissionRepository permissionRepositoryMock;

    @Mock
    private PermissionService permissionServiceMock;

    @Autowired
    private PermissionService permissionService;

    /**
     * This repository is mocked in the my.com.view.repository.search test package.
     *
     * @see my.com.view.repository.search.PermissionSearchRepositoryMockConfiguration
     */
    @Autowired
    private PermissionSearchRepository mockPermissionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPermissionMockMvc;

    private Permission permission;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PermissionResource permissionResource = new PermissionResource(permissionService);
        this.restPermissionMockMvc = MockMvcBuilders.standaloneSetup(permissionResource)
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
    public static Permission createEntity(EntityManager em) {
        Permission permission = new Permission()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .action(DEFAULT_ACTION);
        return permission;
    }

    @Before
    public void initTest() {
        permission = createEntity(em);
    }

    @Test
    @Transactional
    public void createPermission() throws Exception {
        int databaseSizeBeforeCreate = permissionRepository.findAll().size();

        // Create the Permission
        restPermissionMockMvc.perform(post("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permission)))
            .andExpect(status().isCreated());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate + 1);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPermission.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPermission.getAction()).isEqualTo(DEFAULT_ACTION);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(1)).save(testPermission);
    }

    @Test
    @Transactional
    public void createPermissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = permissionRepository.findAll().size();

        // Create the Permission with an existing ID
        permission.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionMockMvc.perform(post("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permission)))
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(0)).save(permission);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setName(null);

        // Create the Permission, which fails.

        restPermissionMockMvc.perform(post("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permission)))
            .andExpect(status().isBadRequest());

        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPermissions() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList
        restPermissionMockMvc.perform(get("/api/permissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())));
    }

    public void getAllPermissionsWithEagerRelationshipsIsEnabled() throws Exception {
        PermissionResource permissionResource = new PermissionResource(permissionServiceMock);
        when(permissionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restPermissionMockMvc = MockMvcBuilders.standaloneSetup(permissionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPermissionMockMvc.perform(get("/api/permissions?eagerload=true"))
        .andExpect(status().isOk());

        verify(permissionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllPermissionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        PermissionResource permissionResource = new PermissionResource(permissionServiceMock);
            when(permissionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restPermissionMockMvc = MockMvcBuilders.standaloneSetup(permissionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPermissionMockMvc.perform(get("/api/permissions?eagerload=true"))
        .andExpect(status().isOk());

            verify(permissionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get the permission
        restPermissionMockMvc.perform(get("/api/permissions/{id}", permission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(permission.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingPermission() throws Exception {
        // Get the permission
        restPermissionMockMvc.perform(get("/api/permissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePermission() throws Exception {
        // Initialize the database
        permissionService.save(permission);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPermissionSearchRepository);

        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Update the permission
        Permission updatedPermission = permissionRepository.findById(permission.getId()).get();
        // Disconnect from session so that the updates on updatedPermission are not directly saved in db
        em.detach(updatedPermission);
        updatedPermission
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .action(UPDATED_ACTION);

        restPermissionMockMvc.perform(put("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPermission)))
            .andExpect(status().isOk());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPermission.getAction()).isEqualTo(UPDATED_ACTION);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(1)).save(testPermission);
    }

    @Test
    @Transactional
    public void updateNonExistingPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Create the Permission

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPermissionMockMvc.perform(put("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permission)))
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(0)).save(permission);
    }

    @Test
    @Transactional
    public void deletePermission() throws Exception {
        // Initialize the database
        permissionService.save(permission);

        int databaseSizeBeforeDelete = permissionRepository.findAll().size();

        // Get the permission
        restPermissionMockMvc.perform(delete("/api/permissions/{id}", permission.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Permission in Elasticsearch
        verify(mockPermissionSearchRepository, times(1)).deleteById(permission.getId());
    }

    @Test
    @Transactional
    public void searchPermission() throws Exception {
        // Initialize the database
        permissionService.save(permission);
        when(mockPermissionSearchRepository.search(queryStringQuery("id:" + permission.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(permission), PageRequest.of(0, 1), 1));
        // Search the permission
        restPermissionMockMvc.perform(get("/api/_search/permissions?query=id:" + permission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Permission.class);
        Permission permission1 = new Permission();
        permission1.setId(1L);
        Permission permission2 = new Permission();
        permission2.setId(permission1.getId());
        assertThat(permission1).isEqualTo(permission2);
        permission2.setId(2L);
        assertThat(permission1).isNotEqualTo(permission2);
        permission1.setId(null);
        assertThat(permission1).isNotEqualTo(permission2);
    }
}
