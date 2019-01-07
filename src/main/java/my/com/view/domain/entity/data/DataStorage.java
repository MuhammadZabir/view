package my.com.view.domain.entity.data;

public class DataStorage {
    private String name;
    private String staffId;
    private String issueName;
    private String description;
    private String difficulty;
    private String department;
    private String status;
    private String startDate;
    private String endDate;
    private String actualEndDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(String actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    @Override
    public String toString() {
        return "DataStorage{" +
            "name='" + name + '\'' +
            ", staffId='" + staffId + '\'' +
            ", issueName='" + issueName + '\'' +
            ", description='" + description + '\'' +
            ", difficulty='" + difficulty + '\'' +
            ", department='" + department + '\'' +
            ", status='" + status + '\'' +
            ", startDate='" + startDate + '\'' +
            ", endDate='" + endDate + '\'' +
            ", actualEndDate='" + actualEndDate + '\'' +
            '}';
    }
}
