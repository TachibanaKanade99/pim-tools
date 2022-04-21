package vn.elca.training.service;

import org.springframework.data.domain.Page;
import vn.elca.training.model.dto.ProjectDto;
import vn.elca.training.model.entity.Project;
import vn.elca.training.model.exception.DeletedProjectStatusNotNewException;
import vn.elca.training.model.exception.EmployeeVisaDoesNotExistException;
import vn.elca.training.model.exception.GroupLeaderDoesNotExistException;
import vn.elca.training.model.exception.ProjectNotFoundException;
import vn.elca.training.model.exception.ProjectNumberAlreadyExistsException;
import vn.elca.training.model.exception.ProjectStatusDoesNotExistException;
import vn.elca.training.model.exception.StartDateIsAfterEndDateException;

import java.util.List;

/**
 * @author vlp
 */
public interface ProjectService {
    Project create(ProjectDto projectDto)
            throws ProjectNumberAlreadyExistsException,
            EmployeeVisaDoesNotExistException,
            GroupLeaderDoesNotExistException,
            ProjectStatusDoesNotExistException,
            StartDateIsAfterEndDateException;
    
    ProjectDto get(Long id);
    
    Project update(Long id, ProjectDto projectDto)
            throws EmployeeVisaDoesNotExistException,
            GroupLeaderDoesNotExistException,
            ProjectStatusDoesNotExistException,
            StartDateIsAfterEndDateException;
    
    List<Project> delete(List<Long> projectIds) throws DeletedProjectStatusNotNewException, ProjectNotFoundException;

    Page<Project> find(String searchKeyword, String status, int page);
}
