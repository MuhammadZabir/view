package my.com.view.repository;

import my.com.view.domain.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Permission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query(value = "select distinct permission from Permission permission left join fetch permission.roles",
        countQuery = "select count(distinct permission) from Permission permission")
    Page<Permission> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct permission from Permission permission left join fetch permission.roles")
    List<Permission> findAllWithEagerRelationships();

    @Query("select permission from Permission permission left join fetch permission.roles where permission.id =:id")
    Optional<Permission> findOneWithEagerRelationships(@Param("id") Long id);

}
