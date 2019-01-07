package my.com.view.domain.entity;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "company")
@Document(indexName = "company")
public class Company extends AbstractAuditingEntity {

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "company")
    private Set<Department> departments = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "point_config_id")
    private PointConfig pointConfig;

    public String getName() {
        return name;
    }

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public Company departments(Set<Department> departments) {
        this.departments = departments;
        return this;
    }

    public Company addDepartment(Department department) {
        this.departments.add(department);
        department.setCompany(this);
        return this;
    }

    public Company removeDepartment(Department department) {
        this.departments.remove(department);
        department.setCompany(null);
        return this;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Company pointConfig(PointConfig pointConfig) {
        this.pointConfig = pointConfig;
        return this;
    }

    public PointConfig getPointConfig() {
        return pointConfig;
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
        Company company = (Company) o;
        if (company.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Company{" +
            "name='" + getName() + "'" +
            "}";
    }
}
