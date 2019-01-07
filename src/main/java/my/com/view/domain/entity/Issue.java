package my.com.view.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "issue")
@Document(indexName = "issue")
public class Issue extends AbstractAuditingEntity {

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "duration_start")
    private ZonedDateTime durationStart;

    @Column(name = "expected_duration_end")
    private ZonedDateTime expectedDurationEnd;

    @Column(name = "duration_end")
    private ZonedDateTime durationEnd;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "difficulty")
    private String difficulty;

    @OneToMany(mappedBy = "issue")
    private Set<CommentIssue> commentIssues = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("")
    private User user;

    public String getName() {
        return name;
    }

    public Issue name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public Issue type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public Issue category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ZonedDateTime getDurationStart() {
        return durationStart;
    }

    public Issue durationStart(ZonedDateTime durationStart) {
        this.durationStart = durationStart;
        return this;
    }

    public void setDurationStart(ZonedDateTime durationStart) {
        this.durationStart = durationStart;
    }

    public ZonedDateTime getExpectedDurationEnd() {
        return expectedDurationEnd;
    }

    public Issue expectedDurationEnd(ZonedDateTime expectedDurationEnd) {
        this.expectedDurationEnd = expectedDurationEnd;
        return this;
    }

    public void setExpectedDurationEnd(ZonedDateTime expectedDurationEnd) {
        this.expectedDurationEnd = expectedDurationEnd;
    }

    public ZonedDateTime getDurationEnd() {
        return durationEnd;
    }

    public Issue durationEnd(ZonedDateTime durationEnd) {
        this.durationEnd = durationEnd;
        return this;
    }

    public void setDurationEnd(ZonedDateTime durationEnd) {
        this.durationEnd = durationEnd;
    }

    public String getDescription() {
        return description;
    }

    public Issue description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public Issue status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<CommentIssue> getCommentIssues() {
        return commentIssues;
    }

    public Issue commentIssues(Set<CommentIssue> commentIssues) {
        this.commentIssues = commentIssues;
        return this;
    }

    public Issue addCommentIssue(CommentIssue commentIssue) {
        this.commentIssues.add(commentIssue);
        commentIssue.setIssue(this);
        return this;
    }

    public Issue removeCommentIssue(CommentIssue commentIssue) {
        this.commentIssues.remove(commentIssue);
        commentIssue.setIssue(null);
        return this;
    }

    public void setCommentIssues(Set<CommentIssue> commentIssues) {
        this.commentIssues = commentIssues;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public User getUser() {
        return user;
    }

    public Issue user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Issue issue = (Issue) o;
        if (issue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Issue{" +
            "name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", category='" + category + '\'' +
            ", durationStart=" + durationStart +
            ", expectedDurationEnd=" + expectedDurationEnd +
            ", durationEnd=" + durationEnd +
            ", description='" + description + '\'' +
            ", status='" + status + '\'' +
            ", commentIssues=" + commentIssues +
            ", difficulty='" + difficulty + '\'' +
            ", user=" + user +
            '}';
    }
}
