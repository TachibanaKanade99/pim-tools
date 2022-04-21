package vn.elca.training.model.dao;

import vn.elca.training.model.entity.GroupTable;

public interface GroupTableDao {

    GroupTable findGroupTableByGroupLeaderId(Long groupLeaderId);
}
