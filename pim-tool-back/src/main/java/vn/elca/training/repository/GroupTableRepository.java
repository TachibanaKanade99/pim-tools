package vn.elca.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import vn.elca.training.model.entity.GroupTable;

public interface GroupTableRepository extends JpaRepository<GroupTable, Long>, QuerydslPredicateExecutor<GroupTable> {
}
