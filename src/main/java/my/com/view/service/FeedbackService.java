package my.com.view.service;

import my.com.view.domain.entity.Feedback;
import my.com.view.repository.FeedbackRepository;
import my.com.view.repository.search.FeedbackSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Feedback.
 */
@Service
@Transactional
public class FeedbackService {

    private final Logger log = LoggerFactory.getLogger(FeedbackService.class);

    private final FeedbackRepository feedbackRepository;

    private final FeedbackSearchRepository feedbackSearchRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, FeedbackSearchRepository feedbackSearchRepository) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackSearchRepository = feedbackSearchRepository;
    }

    /**
     * Save a feedback.
     *
     * @param feedback the entity to save
     * @return the persisted entity
     */
    public Feedback save(Feedback feedback) {
        log.debug("Request to save Feedback : {}", feedback);        Feedback result = feedbackRepository.save(feedback);
        feedbackSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the feedbacks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Feedback> findAll(Pageable pageable) {
        log.debug("Request to get all Feedbacks");
        return feedbackRepository.findAll(pageable);
    }


    /**
     * Get one feedback by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Feedback> findOne(Long id) {
        log.debug("Request to get Feedback : {}", id);
        return feedbackRepository.findById(id);
    }

    /**
     * Delete the feedback by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Feedback : {}", id);
        feedbackRepository.deleteById(id);
        feedbackSearchRepository.deleteById(id);
    }

    /**
     * Search for the feedback corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Feedback> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Feedbacks for query {}", query);
        return feedbackSearchRepository.search(queryStringQuery(query), pageable);    }
}
