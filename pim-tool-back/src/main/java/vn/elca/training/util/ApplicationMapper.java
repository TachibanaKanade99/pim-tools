package vn.elca.training.util;

import org.springframework.stereotype.Component;
import vn.elca.training.model.dto.GroupDto;
import vn.elca.training.model.dto.ProjectDto;
import vn.elca.training.model.entity.Employee;
import vn.elca.training.model.entity.GroupTable;
import vn.elca.training.model.entity.Project;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gtn
 */
@Component
public class ApplicationMapper {
    public ApplicationMapper() {
        // Mapper utility class
    }

    public ProjectDto projectToProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setProjectNumber(project.getProjectNumber());
        projectDto.setName(project.getName());
        projectDto.setCustomer(project.getCustomer());
        projectDto.setStatus(project.getStatus().toString());
        projectDto.setStartDate(project.getStartDate());
        projectDto.setEndDate(project.getEndDate());
        projectDto.setVersion(project.getVersion());

        return projectDto;
    }

    public ProjectDto projectToEditProjectDto(Project project, Employee projectGroupLeader, List<Employee> employees) {
        ProjectDto projectDto = projectToProjectDto(project);
        projectDto.setGroupLeaderVisa(projectGroupLeader.getVisa());
        projectDto.setEmployeeVisas(employees.stream().map(Employee::getVisa).collect(Collectors.toSet()));

        return projectDto;
    }

    public GroupDto groupTableToGroupDto(GroupTable groupTable) {
        GroupDto groupDto = new GroupDto();
        groupDto.setEmployeeVisa(groupTable.getEmployee().getVisa());

        return groupDto;
    }
}
