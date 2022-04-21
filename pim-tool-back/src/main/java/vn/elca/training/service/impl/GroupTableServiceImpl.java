package vn.elca.training.service.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.elca.training.model.dto.GroupDto;
import vn.elca.training.model.entity.Employee;
import vn.elca.training.model.entity.GroupTable;
import vn.elca.training.model.entity.QEmployee;
import vn.elca.training.model.entity.QGroupTable;
import vn.elca.training.repository.GroupTableRepository;
import vn.elca.training.service.GroupTableService;
import vn.elca.training.util.ApplicationMapper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class GroupTableServiceImpl implements GroupTableService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GroupTableRepository groupTableRepository;

    @Autowired
    private ApplicationMapper mapper;

    @Override
    public Set<Long> listAllGroupIds() {
        Iterator<GroupTable> groupIterator = groupTableRepository.findAll().iterator();
        Set<Long> ids = new HashSet<>();

        while (groupIterator.hasNext()) {
            ids.add(groupIterator.next().getId());
        }
        return ids;
    }

    @Override
    public Set<String> listAllGroupLeaders() {
        Set<Long> ids = this.listAllGroupIds();

        List<Employee> groupLeaders =  new JPAQuery<Employee>(entityManager)
                .from(QEmployee.employee)
                .innerJoin(QGroupTable.groupTable)
                .on(QEmployee.employee.id.eq(QGroupTable.groupTable.employee.id))
                .fetch();

        return groupLeaders.stream().map(Employee::getVisa).collect(Collectors.toSet());
    }
}
