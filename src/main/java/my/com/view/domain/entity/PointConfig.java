package my.com.view.domain.entity;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "point_config")
@Document(indexName = "pointconfig")
public class PointConfig extends AbstractAuditingEntity {

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pointConfig")
    private Set<IssueDifficulty> issueDifficulties = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pointConfig")
    private Set<StatusCategory> statusCategories = new HashSet<>();

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public PointConfig startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public PointConfig endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Set<IssueDifficulty> getIssueDifficulties() {
        return issueDifficulties;
    }

    public PointConfig issueDifficulties(Set<IssueDifficulty> issueDifficulties) {
        this.issueDifficulties = issueDifficulties;
        return this;
    }

    public PointConfig addIssueDifficulty(IssueDifficulty issueDifficulty) {
        this.issueDifficulties.add(issueDifficulty);
        issueDifficulty.setPointConfig(this);
        return this;
    }

    public PointConfig removeIssueDifficulty(IssueDifficulty issueDifficulty) {
        this.issueDifficulties.remove(issueDifficulty);
        issueDifficulty.setPointConfig(null);
        return this;
    }

    public void setIssueDifficulties(Set<IssueDifficulty> issueDifficulties) {
        this.issueDifficulties = issueDifficulties;
    }

    public Set<StatusCategory> getStatusCategories() {
        return statusCategories;
    }

    public PointConfig statusCategories(Set<StatusCategory> statusCategories) {
        this.statusCategories = statusCategories;
        return this;
    }

    public PointConfig addStatusCategory(StatusCategory statusCategory) {
        this.statusCategories.add(statusCategory);
        statusCategory.setPointConfig(this);
        return this;
    }

    public PointConfig removeStatusCategory(StatusCategory statusCategory) {
        this.statusCategories.remove(statusCategory);
        statusCategory.setPointConfig(null);
        return this;
    }

    public void setStatusCategories(Set<StatusCategory> statusCategories) {
        this.statusCategories = statusCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PointConfig pointConfig = (PointConfig) o;
        if (pointConfig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pointConfig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PointConfig{" +
            "startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
