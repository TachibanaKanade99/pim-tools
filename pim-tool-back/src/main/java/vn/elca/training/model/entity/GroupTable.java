package vn.elca.training.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
* @author phau
* */

@Entity
public class GroupTable extends EntityTable {
    @OneToMany(mappedBy = "groupTable", fetch = FetchType.LAZY)
    private Set<Project> projects;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_leader_id")
    @NotNull
    private Employee employee;

    public GroupTable(){}

    public GroupTable(Integer version) {
        this.version = version;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
