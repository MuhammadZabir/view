package my.com.view.web.rest;

import com.codahale.metrics.annotation.Timed;
import my.com.view.domain.entity.Criteria;
import my.com.view.service.CriteriaService;
import my.com.view.web.rest.errors.BadRequestAlertException;
import my.com.view.web.rest.util.HeaderUtil;
import my.com.view.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Criteria.
 */
@RestController
@RequestMapping("/api")
public class CriteriaResource {

    private final Logger log = LoggerFactory.getLogger(CriteriaResource.class);

    private static final String ENTITY_NAME = "criteria";

    private final CriteriaService criteriaService;

    public CriteriaResource(CriteriaService criteriaService) {
        this.criteriaService = criteriaService;
    }

    /**
     * POST  /criteria : Create a new criteria.
     *
     * @param criteria the criteria to create
     * @return the ResponseEntity with status 201 (Created) and with body the new criteria, or with status 400 (Bad Request) if the criteria has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/criteria")
    @Timed
    public ResponseEntity<Criteria> createCriteria(@Valid @RequestBody Criteria criteria) throws URISyntaxException {
        log.debug("REST request to save Criteria : {}", criteria);
        if (criteria.getId() != null) {
            throw new BadRequestAlertException("A new criteria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Criteria result = criteriaService.save(criteria);
        return ResponseEntity.created(new URI("/api/criteria/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /criteria : Updates an existing criteria.
     *
     * @param criteria the criteria to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated criteria,
     * or with status 400 (Bad Request) if the criteria is not valid,
     * or with status 500 (Internal Server Error) if the criteria couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/criteria")
    @Timed
    public ResponseEntity<Criteria> updateCriteria(@Valid @RequestBody Criteria criteria) throws URISyntaxException {
        log.debug("REST request to update Criteria : {}", criteria);
        if (criteria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Criteria result = criteriaService.save(criteria);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, criteria.getId().toString()))
            .body(result);
    }

    /**
     * GET  /criteria : get all the criteria.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of criteria in body
     */
    @GetMapping("/criteria")
    @Timed
    public ResponseEntity<List<Criteria>> getAllCriteria(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Criteria");
        Page<Criteria> page;
        if (eagerload) {
            page = criteriaService.findAllWithEagerRelationships(pageable);
        } else {
            page = criteriaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/criteria?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /criteria/:id : get the "id" criteria.
     *
     * @param id the id of the criteria to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the criteria, or with status 404 (Not Found)
     */
    @GetMapping("/criteria/{id}")
    @Timed
    public ResponseEntity<Criteria> getCriteria(@PathVariable Long id) {
        log.debug("REST request to get Criteria : {}", id);
        Optional<Criteria> criteria = criteriaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(criteria);
    }

    /**
     * DELETE  /criteria/:id : delete the "id" criteria.
     *
     * @param id the id of the criteria to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/criteria/{id}")
    @Timed
    public ResponseEntity<Void> deleteCriteria(@PathVariable Long id) {
        log.debug("REST request to delete Criteria : {}", id);
        criteriaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/criteria?query=:query : search for the criteria corresponding
     * to the query.
     *
     * @param query the query of the criteria search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/criteria")
    @Timed
    public ResponseEntity<List<Criteria>> searchCriteria(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Criteria for query {}", query);
        Page<Criteria> page = criteriaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/criteria");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
