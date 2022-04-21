package vn.elca.training.model.dao;

import vn.elca.training.model.entity.Project;

public interface ProjectDao {

    Project findProjectAndGroupById(Long id);
}
