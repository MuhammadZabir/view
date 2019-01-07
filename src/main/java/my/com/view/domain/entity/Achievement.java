package my.com.view.domain.entity;


import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "achievement")
@Document(indexName = "achievement")
public class Achievement extends AbstractAuditingEntity {

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToOne
    @JoinColumn(unique = true)
    private Department department;

    @OneToOne
    @JoinColumn(unique = true)
    private Criteria criteria;

    public String getStatus() {
        return status;
    }

    public Achievement status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public Achievement user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Department getDepartment() {
        return department;
    }

    public Achievement department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public Achievement criteria(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Achievement achievement = (Achievement) o;
        if (achievement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), achievement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Achievement{" +
            "status='" + getStatus() + "'" +
            "}";
    }
}
