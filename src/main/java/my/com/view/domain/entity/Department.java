package my.com.view.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "department")
@Document(indexName = "department")
public class Department extends AbstractAuditingEntity {

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("departments")
    private Company company;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "department")
    private Set<Achievement> achievements = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "department")
    private Set<User> user;

    public String getName() {
        return name;
    }

    public Department name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Department description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Company getCompany() {
        return company;
    }

    public Department company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Achievement> getAchievements() {
        return achievements;
    }

    public Department achievements(Set<Achievement> achievements) {
        this.achievements = achievements;
        return this;
    }

    public Department addAchievement(Achievement achievement) {
        this.achievements.add(achievement);
        achievement.setDepartment(this);
        return this;
    }

    public Department removeAchievement(Achievement achievement) {
        this.achievements.remove(achievement);
        achievement.setDepartment(null);
        return this;
    }

    public void setAchievements(Set<Achievement> achievements) {
        this.achievements = achievements;
    }

    public Set<User> getUser() {
        return user;
    }

    public void setUser(Set<User> user) {
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
        Department department = (Department) o;
        if (department.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), department.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Department{" +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", company=" + company +
            ", achievements=" + achievements +
            ", user=" + user +
            '}';
    }
}
