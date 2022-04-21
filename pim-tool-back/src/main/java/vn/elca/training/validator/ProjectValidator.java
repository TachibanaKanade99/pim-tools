package vn.elca.training.validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.elca.training.model.dto.ProjectDto;
import vn.elca.training.model.entity.Project;
import vn.elca.training.model.entity.ProjectStatus;
import vn.elca.training.model.exception.DeletedProjectStatusNotNewException;
import vn.elca.training.model.exception.EmployeeVisaDoesNotExistException;
import vn.elca.training.model.exception.ProjectNotFoundException;
import vn.elca.training.model.exception.ProjectNumberAlreadyExistsException;
import vn.elca.training.model.exception.ProjectStatusDoesNotExistException;
import vn.elca.training.model.exception.StartDateIsAfterEndDateException;
import vn.elca.training.repository.ProjectRepository;
import vn.elca.training.util.ApplicationMapper;

/** 
 * @author phau
*/

@Component
public class ProjectValidator {
    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ApplicationMapper mapper;
    
    public void checkExistedProjectNumber(Integer projectNumber) throws ProjectNumberAlreadyExistsException {
        Project foundProject = projectRepository.findByProjectNumber(projectNumber);

        // this condition is to check if the passed project is a new project and has
        // same project number with foundProject
        if (foundProject != null) {
            logger.error(String.format("FOUND EXISTED PROJECT %s WITH PROJECT NUMBER %s ", foundProject.getName(),
                    foundProject.getProjectNumber()));
            throw new ProjectNumberAlreadyExistsException(projectNumber);
        }
    }

    public void checkInvalidVisas(Set<String> requestedVisas, Set<String> validVisas) throws EmployeeVisaDoesNotExistException {
        requestedVisas.removeAll(validVisas);
        Set<String> invalidVisasSet = requestedVisas;

        if (!invalidVisasSet.isEmpty()) {
            String invalidVisas = String.join(", ", invalidVisasSet);
            logger.error("INVALID VISAS: { " + invalidVisas  + " }");
            throw new EmployeeVisaDoesNotExistException(invalidVisas);
        }
    }

    public ProjectStatus checkInvalidStatus(String status) throws ProjectStatusDoesNotExistException {
        ProjectStatus result = null;
        for (ProjectStatus projectStatus: ProjectStatus.values()) {
            if (projectStatus.name().equals(status)) {
                result = projectStatus;
            }
        }
        if (result == null) {
            logger.error(String.format("INVALID PROJECT STATUS %s", status));
            throw new ProjectStatusDoesNotExistException(status);
        }
        return result;
    }

    public void checkStartDateIsAfterEndDate(LocalDate startDate, LocalDate endDate) throws StartDateIsAfterEndDateException {
        if (startDate.isAfter(endDate)) {
            logger.error(String.format("START DATE %s IS AFTER END DATE %s", startDate, endDate));
            throw new StartDateIsAfterEndDateException(startDate, endDate);
        }
    }

    public void checkDeletedProjects(List<Long> ids) throws ProjectNotFoundException, DeletedProjectStatusNotNewException {
        List<Project> projects = projectRepository.findProjectByIdIn(ids);

        // if found projects is empty or their size is less than ids then there exists projects not found (deleted before)
        if (projects.isEmpty() || projects.size() < ids.size()) {
            List<Long> currentIds = projects
                    .stream()
                    .map(Project::getId)
                    .collect(Collectors.toList());
            ids.removeAll(currentIds);
            throw new ProjectNotFoundException(ids);
        }

        List<ProjectDto> invalidProjects = projects
                .stream()
                .map(mapper::projectToProjectDto)
                .filter(project -> !project.getStatus().equals("NEW"))
                .collect(Collectors.toList());

        if (!invalidProjects.isEmpty()) {
            List<String> invalidProjectNames = invalidProjects
                .stream()
                .map(ProjectDto::getName)
                .collect(Collectors.toList());

            String returnError = String.join(", ", invalidProjectNames);
            logger.error(String.format("DELETED PROJECTS %s STATUS NOT NEW", returnError));
            throw new DeletedProjectStatusNotNewException(returnError);
        }
    }
}
