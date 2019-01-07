package my.com.view.web.rest;

import com.codahale.metrics.annotation.Timed;
import my.com.view.domain.entity.PointConfig;
import my.com.view.service.PointConfigService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PointConfig.
 */
@RestController
@RequestMapping("/api")
public class PointConfigResource {

    private final Logger log = LoggerFactory.getLogger(PointConfigResource.class);

    private static final String ENTITY_NAME = "pointConfig";

    private final PointConfigService pointConfigService;

    public PointConfigResource(PointConfigService pointConfigService) {
        this.pointConfigService = pointConfigService;
    }

    /**
     * POST  /point-configs : Create a new pointConfig.
     *
     * @param pointConfig the pointConfig to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pointConfig, or with status 400 (Bad Request) if the pointConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/point-configs")
    @Timed
    public ResponseEntity<PointConfig> createPointConfig(@RequestBody PointConfig pointConfig) throws URISyntaxException {
        log.debug("REST request to save PointConfig : {}", pointConfig);
        if (pointConfig.getId() != null) {
            throw new BadRequestAlertException("A new pointConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PointConfig result = pointConfigService.save(pointConfig);
        return ResponseEntity.created(new URI("/api/point-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /point-configs : Updates an existing pointConfig.
     *
     * @param pointConfig the pointConfig to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pointConfig,
     * or with status 400 (Bad Request) if the pointConfig is not valid,
     * or with status 500 (Internal Server Error) if the pointConfig couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/point-configs")
    @Timed
    public ResponseEntity<PointConfig> updatePointConfig(@RequestBody PointConfig pointConfig) throws URISyntaxException {
        log.debug("REST request to update PointConfig : {}", pointConfig);
        if (pointConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PointConfig result = pointConfigService.save(pointConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pointConfig.getId().toString()))
            .body(result);
    }

    /**
     * GET  /point-configs : get all the pointConfigs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pointConfigs in body
     */
    @GetMapping("/point-configs")
    @Timed
    public ResponseEntity<List<PointConfig>> getAllPointConfigs(Pageable pageable) {
        log.debug("REST request to get a page of PointConfigs");
        Page<PointConfig> page = pointConfigService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/point-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /point-configs/:id : get the "id" pointConfig.
     *
     * @param id the id of the pointConfig to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pointConfig, or with status 404 (Not Found)
     */
    @GetMapping("/point-configs/{id}")
    @Timed
    public ResponseEntity<PointConfig> getPointConfig(@PathVariable Long id) {
        log.debug("REST request to get PointConfig : {}", id);
        Optional<PointConfig> pointConfig = pointConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pointConfig);
    }

    /**
     * DELETE  /point-configs/:id : delete the "id" pointConfig.
     *
     * @param id the id of the pointConfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/point-configs/{id}")
    @Timed
    public ResponseEntity<Void> deletePointConfig(@PathVariable Long id) {
        log.debug("REST request to delete PointConfig : {}", id);
        pointConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/point-configs?query=:query : search for the pointConfig corresponding
     * to the query.
     *
     * @param query the query of the pointConfig search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/point-configs")
    @Timed
    public ResponseEntity<List<PointConfig>> searchPointConfigs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PointConfigs for query {}", query);
        Page<PointConfig> page = pointConfigService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/point-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
