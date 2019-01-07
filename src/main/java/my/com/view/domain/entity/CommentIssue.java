package my.com.view.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "comment_issue")
@Document(indexName = "commentissue")
public class CommentIssue extends AbstractAuditingEntity {

    @NotNull
    @Column(name = "jhi_comment", nullable = false)
    private String comment;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("commentIssues")
    private Issue issue;

    public String getComment() {
        return comment;
    }

    public CommentIssue comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public CommentIssue user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Issue getIssue() {
        return issue;
    }

    public CommentIssue issue(Issue issue) {
        this.issue = issue;
        return this;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommentIssue commentIssue = (CommentIssue) o;
        if (commentIssue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commentIssue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommentIssue{" +
            "comment='" + getComment() + "'" +
            "}";
    }
}
