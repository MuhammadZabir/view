package my.com.view.service;

import my.com.view.domain.entity.Achievement;
import my.com.view.repository.AchievementRepository;
import my.com.view.repository.search.AchievementSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Achievement.
 */
@Service
@Transactional
public class AchievementService {

    private final Logger log = LoggerFactory.getLogger(AchievementService.class);

    private final AchievementRepository achievementRepository;

    private final AchievementSearchRepository achievementSearchRepository;

    public AchievementService(AchievementRepository achievementRepository, AchievementSearchRepository achievementSearchRepository) {
        this.achievementRepository = achievementRepository;
        this.achievementSearchRepository = achievementSearchRepository;
    }

    /**
     * Save a achievement.
     *
     * @param achievement the entity to save
     * @return the persisted entity
     */
    public Achievement save(Achievement achievement) {
        log.debug("Request to save Achievement : {}", achievement);        Achievement result = achievementRepository.save(achievement);
        achievementSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the achievements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Achievement> findAll(Pageable pageable) {
        log.debug("Request to get all Achievements");
        return achievementRepository.findAll(pageable);
    }


    /**
     * Get one achievement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Achievement> findOne(Long id) {
        log.debug("Request to get Achievement : {}", id);
        return achievementRepository.findById(id);
    }

    /**
     * Delete the achievement by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Achievement : {}", id);
        achievementRepository.deleteById(id);
        achievementSearchRepository.deleteById(id);
    }

    /**
     * Search for the achievement corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Achievement> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Achievements for query {}", query);
        return achievementSearchRepository.search(queryStringQuery(query), pageable);    }
}
