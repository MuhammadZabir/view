package my.com.view.web.rest;

import com.codahale.metrics.annotation.Timed;
import my.com.view.domain.entity.CommentIssue;
import my.com.view.service.CommentIssueService;
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
 * REST controller for managing CommentIssue.
 */
@RestController
@RequestMapping("/api")
public class CommentIssueResource {

    private final Logger log = LoggerFactory.getLogger(CommentIssueResource.class);

    private static final String ENTITY_NAME = "commentIssue";

    private final CommentIssueService commentIssueService;

    public CommentIssueResource(CommentIssueService commentIssueService) {
        this.commentIssueService = commentIssueService;
    }

    /**
     * POST  /comment-issues : Create a new commentIssue.
     *
     * @param commentIssue the commentIssue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commentIssue, or with status 400 (Bad Request) if the commentIssue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comment-issues")
    @Timed
    public ResponseEntity<CommentIssue> createCommentIssue(@Valid @RequestBody CommentIssue commentIssue) throws URISyntaxException {
        log.debug("REST request to save CommentIssue : {}", commentIssue);
        if (commentIssue.getId() != null) {
            throw new BadRequestAlertException("A new commentIssue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentIssue result = commentIssueService.save(commentIssue);
        return ResponseEntity.created(new URI("/api/comment-issues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comment-issues : Updates an existing commentIssue.
     *
     * @param commentIssue the commentIssue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commentIssue,
     * or with status 400 (Bad Request) if the commentIssue is not valid,
     * or with status 500 (Internal Server Error) if the commentIssue couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comment-issues")
    @Timed
    public ResponseEntity<CommentIssue> updateCommentIssue(@Valid @RequestBody CommentIssue commentIssue) throws URISyntaxException {
        log.debug("REST request to update CommentIssue : {}", commentIssue);
        if (commentIssue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommentIssue result = commentIssueService.save(commentIssue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commentIssue.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comment-issues : get all the commentIssues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commentIssues in body
     */
    @GetMapping("/comment-issues")
    @Timed
    public ResponseEntity<List<CommentIssue>> getAllCommentIssues(Pageable pageable) {
        log.debug("REST request to get a page of CommentIssues");
        Page<CommentIssue> page = commentIssueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/comment-issues");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /comment-issues/:id : get the "id" commentIssue.
     *
     * @param id the id of the commentIssue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentIssue, or with status 404 (Not Found)
     */
    @GetMapping("/comment-issues/{id}")
    @Timed
    public ResponseEntity<CommentIssue> getCommentIssue(@PathVariable Long id) {
        log.debug("REST request to get CommentIssue : {}", id);
        Optional<CommentIssue> commentIssue = commentIssueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentIssue);
    }

    /**
     * DELETE  /comment-issues/:id : delete the "id" commentIssue.
     *
     * @param id the id of the commentIssue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comment-issues/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommentIssue(@PathVariable Long id) {
        log.debug("REST request to delete CommentIssue : {}", id);
        commentIssueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/comment-issues?query=:query : search for the commentIssue corresponding
     * to the query.
     *
     * @param query the query of the commentIssue search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/comment-issues")
    @Timed
    public ResponseEntity<List<CommentIssue>> searchCommentIssues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CommentIssues for query {}", query);
        Page<CommentIssue> page = commentIssueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/comment-issues");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
