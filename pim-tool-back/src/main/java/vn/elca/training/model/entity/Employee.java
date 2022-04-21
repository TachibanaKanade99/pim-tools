package vn.elca.training.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;


/**
 * @author phau
**/

@Entity
public class Employee extends EntityTable {
    @Column(length = 3)
    @NotNull
    private String visa;

    @Column(length = 50)
    @NotNull
    private String firstName;

    @Column(length = 50)
    @NotNull
    private String lastName;

    @Column
    @NotNull
    private LocalDate birthDate;

    @ManyToMany(
        cascade = { CascadeType.PERSIST, CascadeType.MERGE },
        mappedBy = "employees",
        targetEntity = Project.class
    )
    private Set<Project> projects;

    public Employee() {}

    public Employee(String visa, String firstName, String lastName, LocalDate birthDate, Integer version) {
        this.visa = visa;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.version = version;
    }

    public void setVisa(String visa) {
        this.visa = visa;
    }

    public String getVisa() {
        return visa;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Project> getProjects() {
        return projects;
    }
}
