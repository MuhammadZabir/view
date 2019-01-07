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
@Table(name = "status_category")
@Document(indexName = "statuscategory")
public class StatusCategory extends AbstractAuditingEntity {

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "point")
    private Double point;

    @Column(name = "main")
    private Boolean main;

    @ManyToOne
    @JsonIgnoreProperties("statusCategories")
    private PointConfig pointConfig;

    public String getName() {
        return name;
    }

    public StatusCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPoint() {
        return point;
    }

    public StatusCategory point(Double point) {
        this.point = point;
        return this;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public Boolean isMain() {
        return main;
    }

    public StatusCategory main(Boolean main) {
        this.main = main;
        return this;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public PointConfig getPointConfig() {
        return pointConfig;
    }

    public StatusCategory pointConfig(PointConfig pointConfig) {
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
        StatusCategory statusCategory = (StatusCategory) o;
        if (statusCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), statusCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StatusCategory{" +
            "name='" + getName() + "'" +
            ", point=" + getPoint() +
            ", main='" + isMain() + "'" +
            "}";
    }
}
