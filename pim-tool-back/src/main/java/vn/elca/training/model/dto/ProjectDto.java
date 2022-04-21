package vn.elca.training.model.dto;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author phau
 *
 */
public class ProjectDto {
    private Long id;
    private String groupLeaderVisa;
    private Integer projectNumber;
    private String name;
    private String customer;
    private Set<String> employeeVisas;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer version;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupLeaderVisa() {
        return groupLeaderVisa;
    }

    public void setGroupLeaderVisa(String groupLeaderVisa) {
        this.groupLeaderVisa = groupLeaderVisa;
    }

    public Integer getProjectNumber() {
        return this.projectNumber;
    }

    public void setProjectNumber(Integer projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomer() {
        return this.customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Set<String> getEmployeeVisas() {
        return this.employeeVisas;
    }

    public void setEmployeeVisas(Set<String> employeeVisas) {
        this.employeeVisas = employeeVisas;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
