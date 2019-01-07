package my.com.view.repository;

import my.com.view.domain.entity.PointConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PointConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointConfigRepository extends JpaRepository<PointConfig, Long> {

}
