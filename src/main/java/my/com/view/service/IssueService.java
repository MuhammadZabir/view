package my.com.view.service;

import my.com.view.domain.entity.Issue;
import my.com.view.repository.IssueRepository;
import my.com.view.repository.search.IssueSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Issue.
 */
@Service
@Transactional
public class IssueService {

    private final Logger log = LoggerFactory.getLogger(IssueService.class);

    private final IssueRepository issueRepository;

    private final IssueSearchRepository issueSearchRepository;

    public IssueService(IssueRepository issueRepository, IssueSearchRepository issueSearchRepository) {
        this.issueRepository = issueRepository;
        this.issueSearchRepository = issueSearchRepository;
    }

    /**
     * Save a issue.
     *
     * @param issue the entity to save
     * @return the persisted entity
     */
    public Issue save(Issue issue) {
        log.debug("Request to save Issue : {}", issue);        Issue result = issueRepository.save(issue);
        issueSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the issues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Issue> findAll(Pageable pageable) {
        log.debug("Request to get all Issues");
        return issueRepository.findAll(pageable);
    }


    /**
     * Get one issue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Issue> findOne(Long id) {
        log.debug("Request to get Issue : {}", id);
        return issueRepository.findById(id);
    }

    /**
     * Delete the issue by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Issue : {}", id);
        issueRepository.deleteById(id);
        issueSearchRepository.deleteById(id);
    }

    /**
     * Search for the issue corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Issue> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Issues for query {}", query);
        return issueSearchRepository.search(queryStringQuery(query), pageable);    }
}
