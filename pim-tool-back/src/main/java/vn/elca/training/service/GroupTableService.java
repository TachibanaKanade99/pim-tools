package vn.elca.training.service;

import java.util.Set;

public interface GroupTableService {
    Set<Long> listAllGroupIds();

    Set<String> listAllGroupLeaders();
}
