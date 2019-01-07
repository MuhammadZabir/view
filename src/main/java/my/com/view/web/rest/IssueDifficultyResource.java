package my.com.view.web.rest;

import com.codahale.metrics.annotation.Timed;
import my.com.view.domain.entity.IssueDifficulty;
import my.com.view.service.IssueDifficultyService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing IssueDifficulty.
 */
@RestController
@RequestMapping("/api")
public class IssueDifficultyResource {

    private final Logger log = LoggerFactory.getLogger(IssueDifficultyResource.class);

    private static final String ENTITY_NAME = "issueDifficulty";

    private final IssueDifficultyService issueDifficultyService;

    public IssueDifficultyResource(IssueDifficultyService issueDifficultyService) {
        this.issueDifficultyService = issueDifficultyService;
    }

    /**
     * POST  /issue-difficulties : Create a new issueDifficulty.
     *
     * @param issueDifficulty the issueDifficulty to create
     * @return the ResponseEntity with status 201 (Created) and with body the new issueDifficulty, or with status 400 (Bad Request) if the issueDifficulty has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/issue-difficulties")
    @Timed
    public ResponseEntity<IssueDifficulty> createIssueDifficulty(@Valid @RequestBody IssueDifficulty issueDifficulty) throws URISyntaxException {
        log.debug("REST request to save IssueDifficulty : {}", issueDifficulty);
        if (issueDifficulty.getId() != null) {
            throw new BadRequestAlertException("A new issueDifficulty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IssueDifficulty result = issueDifficultyService.save(issueDifficulty);
        return ResponseEntity.created(new URI("/api/issue-difficulties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /issue-difficulties : Updates an existing issueDifficulty.
     *
     * @param issueDifficulty the issueDifficulty to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated issueDifficulty,
     * or with status 400 (Bad Request) if the issueDifficulty is not valid,
     * or with status 500 (Internal Server Error) if the issueDifficulty couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/issue-difficulties")
    @Timed
    public ResponseEntity<IssueDifficulty> updateIssueDifficulty(@Valid @RequestBody IssueDifficulty issueDifficulty) throws URISyntaxException {
        log.debug("REST request to update IssueDifficulty : {}", issueDifficulty);
        if (issueDifficulty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IssueDifficulty result = issueDifficultyService.save(issueDifficulty);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, issueDifficulty.getId().toString()))
            .body(result);
    }

    /**
     * GET  /issue-difficulties : get all the issueDifficulties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of issueDifficulties in body
     */
    @GetMapping("/issue-difficulties")
    @Timed
    public ResponseEntity<List<IssueDifficulty>> getAllIssueDifficulties(Pageable pageable) {
        log.debug("REST request to get a page of IssueDifficulties");
        Page<IssueDifficulty> page = issueDifficultyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/issue-difficulties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /issue-difficulties/:id : get the "id" issueDifficulty.
     *
     * @param id the id of the issueDifficulty to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the issueDifficulty, or with status 404 (Not Found)
     */
    @GetMapping("/issue-difficulties/{id}")
    @Timed
    public ResponseEntity<IssueDifficulty> getIssueDifficulty(@PathVariable Long id) {
        log.debug("REST request to get IssueDifficulty : {}", id);
        Optional<IssueDifficulty> issueDifficulty = issueDifficultyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(issueDifficulty);
    }

    /**
     * DELETE  /issue-difficulties/:id : delete the "id" issueDifficulty.
     *
     * @param id the id of the issueDifficulty to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/issue-difficulties/{id}")
    @Timed
    public ResponseEntity<Void> deleteIssueDifficulty(@PathVariable Long id) {
        log.debug("REST request to delete IssueDifficulty : {}", id);
        issueDifficultyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/issue-difficulties?query=:query : search for the issueDifficulty corresponding
     * to the query.
     *
     * @param query the query of the issueDifficulty search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/issue-difficulties")
    @Timed
    public ResponseEntity<List<IssueDifficulty>> searchIssueDifficulties(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IssueDifficulties for query {}", query);
        Page<IssueDifficulty> page = issueDifficultyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/issue-difficulties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
