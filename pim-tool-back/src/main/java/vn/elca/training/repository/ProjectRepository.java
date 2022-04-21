package vn.elca.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import vn.elca.training.model.entity.Project;
import vn.elca.training.model.entity.ProjectStatus;

import java.util.List;

/**
 * @author vlp
 *
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, QuerydslPredicateExecutor<Project>, JpaSpecificationExecutor<Project> {

    Project findByProjectNumber(Integer projectNumber);

    List<Project> findProjectByIdIn(List<Long> ids);

    List<Project> deleteProjectByStatusAndIdIn(ProjectStatus status, List<Long> ids);
    
}
