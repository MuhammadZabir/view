package my.com.view.service;

import my.com.view.domain.entity.StatusCategory;
import my.com.view.repository.StatusCategoryRepository;
import my.com.view.repository.search.StatusCategorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing StatusCategory.
 */
@Service
@Transactional
public class StatusCategoryService {

    private final Logger log = LoggerFactory.getLogger(StatusCategoryService.class);

    private final StatusCategoryRepository statusCategoryRepository;

    private final StatusCategorySearchRepository statusCategorySearchRepository;

    public StatusCategoryService(StatusCategoryRepository statusCategoryRepository, StatusCategorySearchRepository statusCategorySearchRepository) {
        this.statusCategoryRepository = statusCategoryRepository;
        this.statusCategorySearchRepository = statusCategorySearchRepository;
    }

    /**
     * Save a statusCategory.
     *
     * @param statusCategory the entity to save
     * @return the persisted entity
     */
    public StatusCategory save(StatusCategory statusCategory) {
        log.debug("Request to save StatusCategory : {}", statusCategory);        StatusCategory result = statusCategoryRepository.save(statusCategory);
        statusCategorySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the statusCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StatusCategory> findAll(Pageable pageable) {
        log.debug("Request to get all StatusCategories");
        return statusCategoryRepository.findAll(pageable);
    }


    /**
     * Get one statusCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<StatusCategory> findOne(Long id) {
        log.debug("Request to get StatusCategory : {}", id);
        return statusCategoryRepository.findById(id);
    }

    /**
     * Delete the statusCategory by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StatusCategory : {}", id);
        statusCategoryRepository.deleteById(id);
        statusCategorySearchRepository.deleteById(id);
    }

    /**
     * Search for the statusCategory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StatusCategory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StatusCategories for query {}", query);
        return statusCategorySearchRepository.search(queryStringQuery(query), pageable);    }
}
