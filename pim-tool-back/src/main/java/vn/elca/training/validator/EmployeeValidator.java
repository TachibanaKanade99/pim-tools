package vn.elca.training.validator;

import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import vn.elca.training.model.entity.Employee;
import vn.elca.training.model.entity.QEmployee;
import vn.elca.training.model.entity.QGroupTable;
import vn.elca.training.model.exception.GroupLeaderDoesNotExistException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class EmployeeValidator {

    private Log logger = LogFactory.getLog(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    public Employee checkExistedGroupLeader(String groupLeaderVisa) throws GroupLeaderDoesNotExistException {
        Employee foundGroupLeader = new JPAQuery<Employee>(entityManager)
                .from(QEmployee.employee)
                .innerJoin(QGroupTable.groupTable)
                .on(QEmployee.employee.id.eq(QGroupTable.groupTable.employee.id))
                .where(QEmployee.employee.visa.eq(groupLeaderVisa))
                .fetchFirst();

        if (foundGroupLeader == null) {
            logger.error(String.format("GROUP LEADER VISA %s NOT EXISTED", groupLeaderVisa));
            throw new GroupLeaderDoesNotExistException(groupLeaderVisa);
        }

        return foundGroupLeader;
    }
}
