package vn.elca.training.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.elca.training.model.dao.EmployeeDao;
import vn.elca.training.model.dao.GroupTableDao;
import vn.elca.training.model.dao.ProjectDao;
import vn.elca.training.model.dto.ProjectDto;
import vn.elca.training.model.entity.Employee;
import vn.elca.training.model.entity.GroupTable;
import vn.elca.training.model.entity.Project;
import vn.elca.training.model.entity.ProjectStatus;
import vn.elca.training.model.exception.DeletedProjectStatusNotNewException;
import vn.elca.training.model.exception.EmployeeVisaDoesNotExistException;
import vn.elca.training.model.exception.GroupLeaderDoesNotExistException;
import vn.elca.training.model.exception.ProjectNotFoundException;
import vn.elca.training.model.exception.ProjectNumberAlreadyExistsException;
import vn.elca.training.model.exception.ProjectStatusDoesNotExistException;
import vn.elca.training.model.exception.StartDateIsAfterEndDateException;
import vn.elca.training.repository.ProjectRepository;
import vn.elca.training.service.ProjectService;
import vn.elca.training.specification.ProjectSpecification;
import vn.elca.training.util.ApplicationMapper;
import vn.elca.training.validator.EmployeeValidator;
import vn.elca.training.validator.ProjectValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mysema.commons.lang.CloseableIterator;

/**
 * @author phau
 */
@Service
@Profile("!dummy | dev")
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ApplicationMapper mapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectValidator projectValidator;

    @Autowired
    private EmployeeValidator employeeValidator;

    @Autowired
    private GroupTableDao groupTableDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private ProjectDao projectDao;

    private Project mapRequest(ProjectDto projectDto)
            throws EmployeeVisaDoesNotExistException,
            GroupLeaderDoesNotExistException,
            ProjectStatusDoesNotExistException,
            StartDateIsAfterEndDateException {
        Project project = new Project();

        // validate and return group leader visa
        Employee groupLeader = employeeValidator.checkExistedGroupLeader(projectDto.getGroupLeaderVisa());
        GroupTable groupTable = groupTableDao.findGroupTableByGroupLeaderId(groupLeader.getId());
        project.setGroup(groupTable);

        //employee's visas can be null
        if (projectDto.getEmployeeVisas() != null) {
            CloseableIterator<Employee> employeeQueryResult = employeeDao.getEmployeeByVisas(projectDto.getEmployeeVisas());

            Set<Employee> employees = new HashSet<>();
            while (employeeQueryResult.hasNext()) {
                Employee employee = employeeQueryResult.next();
                employees.add(employee);
            }
            project.setEmployees(employees);

            // check if employee's visa is invalid
            Set<String> validVisas = employees
                    .stream()
                    .map(Employee::getVisa)
                    .collect(Collectors.toSet());

            projectValidator.checkInvalidVisas(projectDto.getEmployeeVisas(), validVisas);
        }

        // validate project status:
        ProjectStatus validatedStatus = projectValidator.checkInvalidStatus(projectDto.getStatus());
        project.setStatus(validatedStatus);

        // validate start date is after end date
        if (projectDto.getEndDate() != null) {
            projectValidator.checkStartDateIsAfterEndDate(projectDto.getStartDate(), projectDto.getEndDate());
            project.setEndDate(projectDto.getEndDate());
        }

        project.setProjectNumber(projectDto.getProjectNumber());
        project.setName(projectDto.getName());
        project.setCustomer(projectDto.getCustomer());
        project.setStartDate(projectDto.getStartDate());
        project.setVersion(projectDto.getVersion());
        return project;
    }

    @Override
    @Transactional(rollbackFor = {
            ProjectNumberAlreadyExistsException.class,
            EmployeeVisaDoesNotExistException.class,
            GroupLeaderDoesNotExistException.class,
            ProjectStatusDoesNotExistException.class,
            StartDateIsAfterEndDateException.class
    })
    public Project create(ProjectDto projectDto) throws
            ProjectNumberAlreadyExistsException,
            EmployeeVisaDoesNotExistException,
            GroupLeaderDoesNotExistException,
            ProjectStatusDoesNotExistException,
            StartDateIsAfterEndDateException {
        projectValidator.checkExistedProjectNumber(projectDto.getProjectNumber());

        Project createdProject = mapRequest(projectDto);
        return projectRepository.save(createdProject);
    }

    @Override
    @Transactional(rollbackFor = {
            ObjectOptimisticLockingFailureException.class,
            EmployeeVisaDoesNotExistException.class,
            GroupLeaderDoesNotExistException.class,
            ProjectStatusDoesNotExistException.class,
            StartDateIsAfterEndDateException.class
    })
    public Project update(Long id, ProjectDto projectDto) throws
            EmployeeVisaDoesNotExistException,
            GroupLeaderDoesNotExistException,
            ProjectStatusDoesNotExistException,
            StartDateIsAfterEndDateException {
        Project updatedProject = mapRequest(projectDto);

        updatedProject.setId(id);
        //update project
        return projectRepository.save(updatedProject);
    }

    @Override
    public ProjectDto get(Long id) {
        Project project = projectDao.findProjectAndGroupById(id);
        Employee projectGroupLeader = employeeDao.findGroupLeaderByGroupId(project.getGroup().getId());

        List<Employee> employees = employeeDao.findEmployeeByProjectId(id);
//        System.out.println(employees.size());
        return mapper.projectToEditProjectDto(project, projectGroupLeader, employees);
    }

    @Override
    @Transactional(rollbackFor = {
            OptimisticLockingFailureException.class,
            DeletedProjectStatusNotNewException.class,
    })
    public List<Project> delete(List<Long> projectIds)
            throws ProjectNotFoundException,
            DeletedProjectStatusNotNewException {

        projectValidator.checkDeletedProjects(projectIds);

        return projectRepository.deleteProjectByStatusAndIdIn(ProjectStatus.NEW, projectIds);
    }

    @Override
    public Page<Project> find(String searchKeyword, String status, int page) {
        return projectRepository.findAll(
                ProjectSpecification.customFindProjects(searchKeyword, status),
                PageRequest.of(page, 4, Sort.by(Sort.Direction.ASC, "projectNumber")));
    }
}
