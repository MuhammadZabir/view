package my.com.view.service;

import my.com.view.domain.entity.Permission;
import my.com.view.repository.PermissionRepository;
import my.com.view.repository.search.PermissionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Permission.
 */
@Service
@Transactional
public class PermissionService {

    private final Logger log = LoggerFactory.getLogger(PermissionService.class);

    private final PermissionRepository permissionRepository;

    private final PermissionSearchRepository permissionSearchRepository;

    public PermissionService(PermissionRepository permissionRepository, PermissionSearchRepository permissionSearchRepository) {
        this.permissionRepository = permissionRepository;
        this.permissionSearchRepository = permissionSearchRepository;
    }

    /**
     * Save a permission.
     *
     * @param permission the entity to save
     * @return the persisted entity
     */
    public Permission save(Permission permission) {
        log.debug("Request to save Permission : {}", permission);        Permission result = permissionRepository.save(permission);
        permissionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the permissions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Permission> findAll(Pageable pageable) {
        log.debug("Request to get all Permissions");
        return permissionRepository.findAll(pageable);
    }

    /**
     * Get all the Permission with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Permission> findAllWithEagerRelationships(Pageable pageable) {
        return permissionRepository.findAllWithEagerRelationships(pageable);
    }


    /**
     * Get one permission by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Permission> findOne(Long id) {
        log.debug("Request to get Permission : {}", id);
        return permissionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the permission by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Permission : {}", id);
        permissionRepository.deleteById(id);
        permissionSearchRepository.deleteById(id);
    }

    /**
     * Search for the permission corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Permission> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Permissions for query {}", query);
        return permissionSearchRepository.search(queryStringQuery(query), pageable);    }
}
