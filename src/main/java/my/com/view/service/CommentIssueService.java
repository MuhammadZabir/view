package my.com.view.service;

import my.com.view.domain.entity.CommentIssue;
import my.com.view.repository.CommentIssueRepository;
import my.com.view.repository.search.CommentIssueSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CommentIssue.
 */
@Service
@Transactional
public class CommentIssueService {

    private final Logger log = LoggerFactory.getLogger(CommentIssueService.class);

    private final CommentIssueRepository commentIssueRepository;

    private final CommentIssueSearchRepository commentIssueSearchRepository;

    public CommentIssueService(CommentIssueRepository commentIssueRepository, CommentIssueSearchRepository commentIssueSearchRepository) {
        this.commentIssueRepository = commentIssueRepository;
        this.commentIssueSearchRepository = commentIssueSearchRepository;
    }

    /**
     * Save a commentIssue.
     *
     * @param commentIssue the entity to save
     * @return the persisted entity
     */
    public CommentIssue save(CommentIssue commentIssue) {
        log.debug("Request to save CommentIssue : {}", commentIssue);        CommentIssue result = commentIssueRepository.save(commentIssue);
        commentIssueSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the commentIssues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommentIssue> findAll(Pageable pageable) {
        log.debug("Request to get all CommentIssues");
        return commentIssueRepository.findAll(pageable);
    }


    /**
     * Get one commentIssue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CommentIssue> findOne(Long id) {
        log.debug("Request to get CommentIssue : {}", id);
        return commentIssueRepository.findById(id);
    }

    /**
     * Delete the commentIssue by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CommentIssue : {}", id);
        commentIssueRepository.deleteById(id);
        commentIssueSearchRepository.deleteById(id);
    }

    /**
     * Search for the commentIssue corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommentIssue> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CommentIssues for query {}", query);
        return commentIssueSearchRepository.search(queryStringQuery(query), pageable);    }
}
