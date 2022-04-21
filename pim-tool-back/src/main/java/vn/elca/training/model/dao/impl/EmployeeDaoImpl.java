package vn.elca.training.model.dao.impl;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;
import vn.elca.training.model.dao.EmployeeDao;
import vn.elca.training.model.entity.Employee;
import vn.elca.training.model.entity.QEmployee;
import vn.elca.training.model.entity.QGroupTable;
import vn.elca.training.model.entity.QProject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Component
public class EmployeeDaoImpl implements EmployeeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CloseableIterator<Employee> getEmployeeByVisas(Set<String> visas) {
        return new JPAQuery<Employee>(entityManager)
                    .from(QEmployee.employee)
                    .where(QEmployee.employee.visa.in(visas))
                    .iterate();
    }

    @Override
    public Employee findGroupLeaderByGroupId(Long groupId) {
        return new JPAQuery<Employee>(entityManager)
                .from(QEmployee.employee)
                .innerJoin(QGroupTable.groupTable)
                .on(QEmployee.employee.id.eq(QGroupTable.groupTable.employee.id))
                .where(QGroupTable.groupTable.id.eq(groupId))
                .fetchFirst();
    }

    @Override
    public List<Employee> findEmployeeByProjectId(Long projectId) {
        return new JPAQuery<Employee>(entityManager)
                .from(QEmployee.employee)
                .innerJoin(QEmployee.employee.projects, QProject.project)
                .where(QProject.project.id.eq(projectId))
                .fetch();
    }
}
