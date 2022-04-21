package vn.elca.training.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.elca.training.model.dto.ProjectDto;
import vn.elca.training.model.dto.ProjectPageDto;
import vn.elca.training.model.entity.Project;
import vn.elca.training.model.exception.DeletedProjectStatusNotNewException;
import vn.elca.training.model.exception.EmployeeVisaDoesNotExistException;
import vn.elca.training.model.exception.GroupLeaderDoesNotExistException;
import vn.elca.training.model.exception.ProjectNotFoundException;
import vn.elca.training.model.exception.ProjectNumberAlreadyExistsException;
import vn.elca.training.model.exception.ProjectStatusDoesNotExistException;
import vn.elca.training.model.exception.StartDateIsAfterEndDateException;
import vn.elca.training.service.ProjectService;

/**
 * @author phau
 */
@RestController
@RequestMapping("/projects")
public class ProjectController extends AbstractApplicationController {

    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/create")
    public ProjectDto create(@RequestBody ProjectDto projectDto)
            throws ProjectNumberAlreadyExistsException,
            EmployeeVisaDoesNotExistException,
            GroupLeaderDoesNotExistException,
            ProjectStatusDoesNotExistException,
            StartDateIsAfterEndDateException {
        return mapper.projectToProjectDto(projectService.create(projectDto));
    }

    @GetMapping(value = "/{id}")
    public ProjectDto get(@PathVariable Long id) {
        return projectService.get(id);
    }

    @PutMapping(value = "/{id}/update")
    public ProjectDto update(@PathVariable Long id, @RequestBody ProjectDto projectDto)
            throws EmployeeVisaDoesNotExistException,
            GroupLeaderDoesNotExistException,
            ProjectStatusDoesNotExistException,
            StartDateIsAfterEndDateException {
        return mapper.projectToProjectDto(projectService.update(id, projectDto));
    }

    @DeleteMapping(value = "/delete")
    public List<ProjectDto> delete(@RequestParam List<Long> ids) throws ProjectNotFoundException, DeletedProjectStatusNotNewException {
        return projectService.delete(ids)
                .stream()
                .map(mapper::projectToProjectDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/find")
    public ProjectPageDto find(
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String status,
            @RequestParam int page) {
        ProjectPageDto resultPage = new ProjectPageDto();
        Page<Project> projectsPage =  projectService.find(searchKeyword, status, page);
        resultPage.setProjects(projectsPage
                .stream()
                .map(mapper::projectToProjectDto)
                .collect(Collectors.toList()));
        resultPage.setTotalPages(projectsPage.getTotalPages());
        return resultPage;
    }
}
