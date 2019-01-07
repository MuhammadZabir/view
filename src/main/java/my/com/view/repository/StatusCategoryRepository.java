package my.com.view.repository;

import my.com.view.domain.entity.StatusCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StatusCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatusCategoryRepository extends JpaRepository<StatusCategory, Long> {

}
