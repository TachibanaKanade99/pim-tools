package vn.elca.training.model.dao.impl;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;
import vn.elca.training.model.dao.GroupTableDao;
import vn.elca.training.model.entity.GroupTable;
import vn.elca.training.model.entity.QEmployee;
import vn.elca.training.model.entity.QGroupTable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class GroupTableDaoImpl implements GroupTableDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GroupTable findGroupTableByGroupLeaderId(Long groupLeaderId) {
        return new JPAQuery<GroupTable>(entityManager)
                .from(QGroupTable.groupTable)
                .innerJoin(QEmployee.employee)
                .on(QGroupTable.groupTable.employee.id.eq(QEmployee.employee.id))
                .where(QEmployee.employee.id.eq(groupLeaderId))
                .fetchFirst();
    }
}
