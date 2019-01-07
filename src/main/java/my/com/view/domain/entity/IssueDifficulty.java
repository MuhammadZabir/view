package my.com.view.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "issue_difficulty")
@Document(indexName = "issuedifficulty")
public class IssueDifficulty extends AbstractAuditingEntity {

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_value")
    private Double value;

    @ManyToOne
    @JsonIgnoreProperties("issueDifficulties")
    private PointConfig pointConfig;

    public String getName() {
        return name;
    }

    public IssueDifficulty name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public IssueDifficulty value(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public PointConfig getPointConfig() {
        return pointConfig;
    }

    public IssueDifficulty pointConfig(PointConfig pointConfig) {
        this.pointConfig = pointConfig;
        return this;
    }

    public void setPointConfig(PointConfig pointConfig) {
        this.pointConfig = pointConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssueDifficulty issueDifficulty = (IssueDifficulty) o;
        if (issueDifficulty.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issueDifficulty.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IssueDifficulty{" +
            "name='" + name + '\'' +
            ", value=" + value +
            ", pointConfig=" + pointConfig +
            '}';
    }
}
