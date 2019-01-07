package my.com.view.service;

import my.com.view.domain.entity.Criteria;
import my.com.view.repository.CriteriaRepository;
import my.com.view.repository.search.CriteriaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Criteria.
 */
@Service
@Transactional
public class CriteriaService {

    private final Logger log = LoggerFactory.getLogger(CriteriaService.class);

    private final CriteriaRepository criteriaRepository;

    private final CriteriaSearchRepository criteriaSearchRepository;

    public CriteriaService(CriteriaRepository criteriaRepository, CriteriaSearchRepository criteriaSearchRepository) {
        this.criteriaRepository = criteriaRepository;
        this.criteriaSearchRepository = criteriaSearchRepository;
    }

    /**
     * Save a criteria.
     *
     * @param criteria the entity to save
     * @return the persisted entity
     */
    public Criteria save(Criteria criteria) {
        log.debug("Request to save Criteria : {}", criteria);        Criteria result = criteriaRepository.save(criteria);
        criteriaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the criteria.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Criteria> findAll(Pageable pageable) {
        log.debug("Request to get all Criteria");
        return criteriaRepository.findAll(pageable);
    }

    /**
     * Get all the Criteria with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Criteria> findAllWithEagerRelationships(Pageable pageable) {
        return criteriaRepository.findAllWithEagerRelationships(pageable);
    }


    /**
     * Get one criteria by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Criteria> findOne(Long id) {
        log.debug("Request to get Criteria : {}", id);
        return criteriaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the criteria by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Criteria : {}", id);
        criteriaRepository.deleteById(id);
        criteriaSearchRepository.deleteById(id);
    }

    /**
     * Search for the criteria corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Criteria> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Criteria for query {}", query);
        return criteriaSearchRepository.search(queryStringQuery(query), pageable);    }
}
