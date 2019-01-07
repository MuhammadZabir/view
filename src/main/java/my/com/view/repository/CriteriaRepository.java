package my.com.view.repository;

import my.com.view.domain.entity.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Criteria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CriteriaRepository extends JpaRepository<Criteria, Long> {

    @Query(value = "select distinct criteria from Criteria criteria left join fetch criteria.criteria",
        countQuery = "select count(distinct criteria) from Criteria criteria")
    Page<Criteria> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct criteria from Criteria criteria left join fetch criteria.criteria")
    List<Criteria> findAllWithEagerRelationships();

    @Query("select criteria from Criteria criteria left join fetch criteria.criteria where criteria.id =:id")
    Optional<Criteria> findOneWithEagerRelationships(@Param("id") Long id);

}
