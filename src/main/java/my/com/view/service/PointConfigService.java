package my.com.view.service;

import my.com.view.domain.entity.PointConfig;
import my.com.view.repository.PointConfigRepository;
import my.com.view.repository.search.PointConfigSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PointConfig.
 */
@Service
@Transactional
public class PointConfigService {

    private final Logger log = LoggerFactory.getLogger(PointConfigService.class);

    private final PointConfigRepository pointConfigRepository;

    private final PointConfigSearchRepository pointConfigSearchRepository;

    public PointConfigService(PointConfigRepository pointConfigRepository, PointConfigSearchRepository pointConfigSearchRepository) {
        this.pointConfigRepository = pointConfigRepository;
        this.pointConfigSearchRepository = pointConfigSearchRepository;
    }

    /**
     * Save a pointConfig.
     *
     * @param pointConfig the entity to save
     * @return the persisted entity
     */
    public PointConfig save(PointConfig pointConfig) {
        log.debug("Request to save PointConfig : {}", pointConfig);        PointConfig result = pointConfigRepository.save(pointConfig);
        pointConfigSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the pointConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PointConfig> findAll(Pageable pageable) {
        log.debug("Request to get all PointConfigs");
        return pointConfigRepository.findAll(pageable);
    }


    /**
     * Get one pointConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PointConfig> findOne(Long id) {
        log.debug("Request to get PointConfig : {}", id);
        return pointConfigRepository.findById(id);
    }

    /**
     * Delete the pointConfig by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PointConfig : {}", id);
        pointConfigRepository.deleteById(id);
        pointConfigSearchRepository.deleteById(id);
    }

    /**
     * Search for the pointConfig corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PointConfig> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PointConfigs for query {}", query);
        return pointConfigSearchRepository.search(queryStringQuery(query), pageable);    }
}
