package my.com.view.service;

import my.com.view.domain.entity.IssueDifficulty;
import my.com.view.repository.IssueDifficultyRepository;
import my.com.view.repository.search.IssueDifficultySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing IssueDifficulty.
 */
@Service
@Transactional
public class IssueDifficultyService {

    private final Logger log = LoggerFactory.getLogger(IssueDifficultyService.class);

    private final IssueDifficultyRepository issueDifficultyRepository;

    private final IssueDifficultySearchRepository issueDifficultySearchRepository;

    public IssueDifficultyService(IssueDifficultyRepository issueDifficultyRepository, IssueDifficultySearchRepository issueDifficultySearchRepository) {
        this.issueDifficultyRepository = issueDifficultyRepository;
        this.issueDifficultySearchRepository = issueDifficultySearchRepository;
    }

    /**
     * Save a issueDifficulty.
     *
     * @param issueDifficulty the entity to save
     * @return the persisted entity
     */
    public IssueDifficulty save(IssueDifficulty issueDifficulty) {
        log.debug("Request to save IssueDifficulty : {}", issueDifficulty);        IssueDifficulty result = issueDifficultyRepository.save(issueDifficulty);
        issueDifficultySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the issueDifficulties.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IssueDifficulty> findAll(Pageable pageable) {
        log.debug("Request to get all IssueDifficulties");
        return issueDifficultyRepository.findAll(pageable);
    }


    /**
     * Get one issueDifficulty by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<IssueDifficulty> findOne(Long id) {
        log.debug("Request to get IssueDifficulty : {}", id);
        return issueDifficultyRepository.findById(id);
    }

    /**
     * Delete the issueDifficulty by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete IssueDifficulty : {}", id);
        issueDifficultyRepository.deleteById(id);
        issueDifficultySearchRepository.deleteById(id);
    }

    /**
     * Search for the issueDifficulty corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IssueDifficulty> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IssueDifficulties for query {}", query);
        return issueDifficultySearchRepository.search(queryStringQuery(query), pageable);    }
}
