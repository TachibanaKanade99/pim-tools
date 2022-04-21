package vn.elca.training.model.dao.impl;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;
import vn.elca.training.model.dao.ProjectDao;
import vn.elca.training.model.entity.Project;
import vn.elca.training.model.entity.QGroupTable;
import vn.elca.training.model.entity.QProject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class ProjectDaoImpl implements ProjectDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Project findProjectAndGroupById(Long id) {
        return new JPAQuery<Project>(entityManager)
                .from(QProject.project)
                .innerJoin(QProject.project.groupTable, QGroupTable.groupTable)
                .where(QProject.project.id.eq(id))
                .fetchFirst();
    }
}
