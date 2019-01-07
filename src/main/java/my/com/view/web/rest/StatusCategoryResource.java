package my.com.view.web.rest;

import com.codahale.metrics.annotation.Timed;
import my.com.view.domain.entity.StatusCategory;
import my.com.view.service.StatusCategoryService;
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
 * REST controller for managing StatusCategory.
 */
@RestController
@RequestMapping("/api")
public class StatusCategoryResource {

    private final Logger log = LoggerFactory.getLogger(StatusCategoryResource.class);

    private static final String ENTITY_NAME = "statusCategory";

    private final StatusCategoryService statusCategoryService;

    public StatusCategoryResource(StatusCategoryService statusCategoryService) {
        this.statusCategoryService = statusCategoryService;
    }

    /**
     * POST  /status-categories : Create a new statusCategory.
     *
     * @param statusCategory the statusCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new statusCategory, or with status 400 (Bad Request) if the statusCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/status-categories")
    @Timed
    public ResponseEntity<StatusCategory> createStatusCategory(@Valid @RequestBody StatusCategory statusCategory) throws URISyntaxException {
        log.debug("REST request to save StatusCategory : {}", statusCategory);
        if (statusCategory.getId() != null) {
            throw new BadRequestAlertException("A new statusCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StatusCategory result = statusCategoryService.save(statusCategory);
        return ResponseEntity.created(new URI("/api/status-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /status-categories : Updates an existing statusCategory.
     *
     * @param statusCategory the statusCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated statusCategory,
     * or with status 400 (Bad Request) if the statusCategory is not valid,
     * or with status 500 (Internal Server Error) if the statusCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/status-categories")
    @Timed
    public ResponseEntity<StatusCategory> updateStatusCategory(@Valid @RequestBody StatusCategory statusCategory) throws URISyntaxException {
        log.debug("REST request to update StatusCategory : {}", statusCategory);
        if (statusCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StatusCategory result = statusCategoryService.save(statusCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, statusCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /status-categories : get all the statusCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of statusCategories in body
     */
    @GetMapping("/status-categories")
    @Timed
    public ResponseEntity<List<StatusCategory>> getAllStatusCategories(Pageable pageable) {
        log.debug("REST request to get a page of StatusCategories");
        Page<StatusCategory> page = statusCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/status-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /status-categories/:id : get the "id" statusCategory.
     *
     * @param id the id of the statusCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the statusCategory, or with status 404 (Not Found)
     */
    @GetMapping("/status-categories/{id}")
    @Timed
    public ResponseEntity<StatusCategory> getStatusCategory(@PathVariable Long id) {
        log.debug("REST request to get StatusCategory : {}", id);
        Optional<StatusCategory> statusCategory = statusCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(statusCategory);
    }

    /**
     * DELETE  /status-categories/:id : delete the "id" statusCategory.
     *
     * @param id the id of the statusCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/status-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteStatusCategory(@PathVariable Long id) {
        log.debug("REST request to delete StatusCategory : {}", id);
        statusCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/status-categories?query=:query : search for the statusCategory corresponding
     * to the query.
     *
     * @param query the query of the statusCategory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/status-categories")
    @Timed
    public ResponseEntity<List<StatusCategory>> searchStatusCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StatusCategories for query {}", query);
        Page<StatusCategory> page = statusCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/status-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
