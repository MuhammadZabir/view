package my.com.view.domain.entity;


import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "criteria")
@Document(indexName = "criteria")
public class Criteria extends AbstractAuditingEntity {

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "jhi_level")
    private Long level;

    @Column(name = "jhi_type")
    private String type;

    @ManyToMany
    @JoinTable(name = "criteria_criteria",
               joinColumns = @JoinColumn(name = "criteria_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "criteria2_id", referencedColumnName = "id"))
    private Set<Criteria> criteria = new HashSet<>();

    public String getName() {
        return name;
    }

    public Criteria name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Criteria description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLevel() {
        return level;
    }

    public Criteria level(Long level) {
        this.level = level;
        return this;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public Criteria type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Criteria> getCriteria() {
        return criteria;
    }

    public Criteria criteria(Set<Criteria> criteria) {
        this.criteria = criteria;
        return this;
    }

    public Criteria addCriteria(Criteria criteria) {
        this.criteria.add(criteria);
        criteria.getCriteria().add(this);
        return this;
    }

    public Criteria removeCriteria(Criteria criteria) {
        this.criteria.remove(criteria);
        criteria.getCriteria().remove(this);
        return this;
    }

    public void setCriteria(Set<Criteria> criteria) {
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
        Criteria criteria = (Criteria) o;
        if (criteria.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), criteria.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Criteria{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", level=" + getLevel() +
            ", type='" + getType() + "'" +
            "}";
    }
}
