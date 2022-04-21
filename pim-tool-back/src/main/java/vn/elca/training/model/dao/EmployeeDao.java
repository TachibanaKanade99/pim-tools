package vn.elca.training.model.dao;

import com.mysema.commons.lang.CloseableIterator;
import vn.elca.training.model.entity.Employee;

import java.util.List;
import java.util.Set;

public interface EmployeeDao {

    CloseableIterator<Employee> getEmployeeByVisas(Set<String> visas);

    Employee findGroupLeaderByGroupId(Long groupId);

    List<Employee> findEmployeeByProjectId(Long projectId);
}
