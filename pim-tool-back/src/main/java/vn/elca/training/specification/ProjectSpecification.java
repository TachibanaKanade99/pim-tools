package vn.elca.training.specification;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import vn.elca.training.model.entity.Project;

public class ProjectSpecification {
    private ProjectSpecification() {
    }

    public static Specification<Project> customFindProjects(String searchKeyword, String status) {
        /*
        * Both searchKey and search columns: name, customer are lowered to support searching with ignore cases
        * */

        String searchKey = "%" + searchKeyword.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicateProject = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("projectNumber").as(String.class), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("customer")), searchKey)
            );

            if (status != null && !status.equals("")) {
                Predicate predicateProjectStatus = criteriaBuilder.equal(root.get("status").as(String.class), status);
                return criteriaBuilder.and(predicateProject, predicateProjectStatus);
            }
            return predicateProject;
        };
    }
}