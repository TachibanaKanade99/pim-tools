package vn.elca.training.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import java.util.Set;

/**
 * @author vlp
 */
@Entity
public class Project extends EntityTable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @NotNull
    private GroupTable groupTable;

    @Column(unique = true, length = 4)
    @NotNull
    private Integer projectNumber;

    @Column(length = 50)
    @NotNull
    private String name;

    @Column(length = 50)
    @NotNull
    private String customer;

    @Column(length = 3)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column
    @NotNull
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @ManyToMany(
        targetEntity = Employee.class,
        cascade = { CascadeType.PERSIST, CascadeType.MERGE }
    )
    @JoinTable(
        name = "project_employee",
        joinColumns = { @JoinColumn(name = "project_id") },
        inverseJoinColumns = { @JoinColumn(name = "employee_id") }
    )
    private Set<Employee> employees;

    public Project() {}

    public Project(Integer projectNumber, String name, String customer, ProjectStatus status, LocalDate startDate, LocalDate endDate) {
        this.projectNumber = projectNumber;
        this.name = name;
        this.customer = customer;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setGroup(GroupTable groupTable) {
        this.groupTable = groupTable;
    }

    public GroupTable getGroup() {
        return groupTable;
    }

    public void setProjectNumber(Integer projectNumber) {
        this.projectNumber = projectNumber;
    }

    public Integer getProjectNumber() {
        return projectNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }
}