package my.com.view.repository;

import my.com.view.domain.entity.CommentIssue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the CommentIssue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentIssueRepository extends JpaRepository<CommentIssue, Long> {

    @Query("select comment_issue from CommentIssue comment_issue where comment_issue.user.login = ?#{principal.username}")
    List<CommentIssue> findByUserIsCurrentUser();

}
