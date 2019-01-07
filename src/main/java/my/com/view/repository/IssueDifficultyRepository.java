package my.com.view.repository;

import my.com.view.domain.entity.IssueDifficulty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IssueDifficulty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssueDifficultyRepository extends JpaRepository<IssueDifficulty, Long> {

}
